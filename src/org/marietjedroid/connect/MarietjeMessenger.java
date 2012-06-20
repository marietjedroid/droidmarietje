/**
 * @licence GNU General Public licence http://www.gnu.org/copyleft/gpl.html
 * @Copyright (C) 2012 Thom Wiggers
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.marietjedroid.connect;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.http.HttpResponse;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

/**
 * Handles most of the messaging aspect
 * 
 * @author Thom
 * 
 */
public abstract class MarietjeMessenger extends Observable {
	private HttpClient httpClient = new DefaultHttpClient();

	final Lock lock = new ReentrantLock();

	private Semaphore messageInSemaphore = new Semaphore(0);
	private Semaphore outSemaphore = new Semaphore(0);

	protected Boolean running = false;
	/**
	 * Channel token
	 */
	private String token = null;

	private Queue<JSONObject> queueMessageIn = new LinkedList<JSONObject>();

	private Queue<JSONObject> queueOut = new LinkedList<JSONObject>();
	
	private Semaphore messagesInSemaphore = new Semaphore(1);
	
	private Integer nPending = 0;

	private final String host;
	private final String path;
	private final int port;

	public MarietjeMessenger(String host, int port, String path, String token) {
		this(host, port, path);
		this.token = token;
	}

	public MarietjeMessenger(String host, int port, String path) {
		this.host = host;
		this.port = port;
		this.path = path;
		this.token = generateToken();
	}

	/**
	 * Generates a new token
	 * 
	 * @return a new token
	 */
	protected static String generateToken() {
		SecureRandom random = new SecureRandom();
		while (true) {
			String attempt = new BigInteger(130, random).toString();
			attempt = new String(
					org.apache.commons.codec.binary.Base64.encodeBase64(attempt
							.getBytes())).substring(0, 7);

			return attempt;
		}
	}

	/**
	 * Sends a stream
	 * 
	 * FIXME probably massively broken
	 * 
	 * @param token
	 * @param stream
	 * @param blocking
	 */
	public void sendStream(String token, ContentBody stream, boolean blocking) {
		if (!this.token.equals(token))
			throw new IllegalArgumentException("Wrong token!");

		MultipartEntity multipartStream = new MultipartEntity();
		multipartStream.addPart("stream", stream);

		final HttpPost post = new HttpPost(String.format("http://%s:%s%s",
				host, port, path));
		// FIXME sowieso stuk
		post.setEntity(multipartStream);

		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					httpClient.execute(post);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		t.start();

		if (blocking) {
			try {
				t.join();
			} catch (InterruptedException e) {

			}
		}

	}

	/**
	 * Sends a blocking stream, probably massively broken
	 * 
	 * FIXME
	 * 
	 * @param token
	 * @param stream
	 */
	public void sendStream(String token, ContentBody stream) {
		this.sendStream(token, stream, true);
	}

	/**
	 * @param token
	 * @param message
	 */
	public void sendMessage(JSONObject message) {

		this.queueOut.add(message);
		this.outSemaphore.release();
	}

	/**
	 * Acts on incoming messages
	 * 
	 * @author Thom
	 * 
	 */
	private class MessageDispatcher implements Runnable {

		public void run() {
			try {
				messagesInSemaphore.acquire();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			while (running) {
				while (!queueMessageIn.isEmpty()) {
					try {

						JSONObject data = queueMessageIn.poll();
						messagesInSemaphore.release();
						handleMessage(token, data);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			
				try {
					messageInSemaphore.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Keeps polling marietje
	 * 
	 * @author Thom
	 * 
	 */
	private class Requester implements Runnable {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			while (running) {
				sendMessages();

			}

		}
	}
	
	private void sendMessages() {
		JSONObject[] data = null;
		
		//do we need to execute the wait code?
		// needed for the synchronized.
		Boolean wait = false;
		synchronized(nPending) {
			if ((queueOut.isEmpty() || token == null) && nPending > 0) 
				wait = true;
		}
		if(wait)	
		{
			try {
				outSemaphore.acquire();
			} catch (InterruptedException e) {
			}
			if (!running)
				return;
			sendMessages();
		}
		synchronized (nPending){
			nPending++;
		}
		synchronized(queueOut){
			if (!queueOut.isEmpty()) {
				data = queueOut.toArray(new JSONObject[0]);
				queueOut.clear();
			}
		}
		if (data != null) {
			try {
				doRequest(Arrays.asList(data));
			} catch (MarietjeException e) {
				stop();
			}
		} else {
			try {
				doRequest(null);
			} catch (MarietjeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		synchronized (nPending){
			nPending--;
		}
		synchronized(outSemaphore) {
			if(outSemaphore.availablePermits() <0){
				outSemaphore.release(-outSemaphore.availablePermits());
			}
		}
	}

	/**
	 * Starts running the threads
	 * 
	 * @throws MarietjeException
	 */
	public void run() throws MarietjeException {
		if (this.running)
			throw new IllegalStateException("Already running");
		this.running = true;
		if (this.token != null) {
			this.doRequest(null);
		}
		Thread t1 = new Thread(new MessageDispatcher());
		t1.setDaemon(true);
		t1.start();

		Thread t2 = new Thread(new Requester());
		t2.setDaemon(true);
		t2.start();
	}

	/**
	 * Sends a request and handles the response
	 * 
	 * @param list
	 * @throws MarietjeException
	 */
	private void doRequest(List<JSONObject> list) throws MarietjeException {
		if (list != null)
			list = new ArrayList<JSONObject>(list);
		else
			list = new ArrayList<JSONObject>();

		HttpClient httpClient = new DefaultHttpClient();
		if (this.token == null) {
			throw new IllegalStateException("token is null");
		}

		JSONArray json = new JSONArray();
		json.put(token);
		for (JSONObject m : list)
			json.put(m);
		HttpGet hp = null;
		try {
			System.out.println("JSON: " + json.toString());
			String url = String.format("http://%s:%s%s?m=%s", host, port, path,
					URLEncoder.encode(json.toString(), "UTF-8"));
			System.out.println("url: " + url);
			hp = new HttpGet(url);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		try {
			HttpResponse r = httpClient.execute(hp);
			InputStreamReader is = new InputStreamReader(r.getEntity()
					.getContent());
			BufferedReader br = new BufferedReader(is);
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println("response: " + line);
				sb.append(line);
			}
		} catch (IOException e) {
			MarietjeException tr = new MarietjeException("Connection stuk!"
					+ e.getMessage());
			System.out.println("KAPOT");
			throw tr;
		}

		JSONArray d = null;
		try {
			d = new JSONArray(new JSONTokener(sb.toString()));
		} catch (JSONException e) {
			throw new MarietjeException("Ja, kapot!");
		}

		if (d == null || d.length() != 3)
			throw new MarietjeException("Unexpected length of response list");
		String token = null;
		JSONArray msgs = null;
		try {
			token = d.getString(0);
			msgs = d.getJSONArray(1);
			JSONObject stream = d.getJSONObject(2);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
		}

		synchronized (this.outSemaphore) {
			String oldToken = this.token;
			this.token = token;

			if (oldToken == null) {
				this.outSemaphore.release();
			}
		}
		
		try {
			messagesInSemaphore.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (int i = 0; i < msgs.length(); i++) {
			try {
				this.queueMessageIn.add(msgs.getJSONObject(i));
				this.messageInSemaphore.release();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				messagesInSemaphore.release();
			}
		}

		// TODO Streams left out.

	}
	
	/**
	 * Immediately sends the messages.
	 * 
	 * @param jsonObject
	 */
	protected void sendPriorityMessage(JSONObject jsonObject) {
		this.sendMessage(jsonObject);
		sendMessages();
		
	}

	/**
	 * Handle an imcoming message
	 * 
	 * @param token
	 * @param message
	 */
	protected abstract void handleMessage(String token, JSONObject message)
			throws JSONException;

	/**
	 * Retrieve a stream
	 * 
	 * 
	 * @param streamId
	 */
	protected abstract void retrieveStream(int streamId);

	/**
	 * Stop
	 */
	public void stop() {
		System.out.println("Stopping");
		
		 this.running = false;
		 this.messageInSemaphore.notifyAll();
		 this.outSemaphore.notifyAll();
		 
	}

}
