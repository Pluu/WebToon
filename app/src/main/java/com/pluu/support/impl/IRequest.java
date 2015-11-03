package com.pluu.support.impl;

import java.util.Map;

/**
 * Request Interface
 * Created by PLUUSYSTEM-NEW on 2015-10-29.
 */
public interface IRequest {

	/**
	 * http 통신방법.
	 * @return httpMethod. GET, POST, PUT, DELETE 등등.
	 */
	String getMethod();

	/**
	 * 요청할 target url.
	 * @return 요청할 target url.
	 */
	String getUrl();

	/**
	 * http 요청에 필요한 params.
	 * @return http 요청에 필요한 params.
	 */
	Map<String, String> getParams();

	/**
	 * http 요청에 필요한 headers.
	 * @return http 요청에 필요한 headers.
	 */
	Map<String, String> getHeaders();


}
