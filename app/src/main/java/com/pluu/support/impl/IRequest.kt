package com.pluu.support.impl

/**
 * Request Interface
 * Created by pluu on 2017-04-19.
 */
interface IRequest {

    /**
     * http 통신방법.
     * @return httpMethod. GET, POST, PUT, DELETE 등등.
     */
    val method: REQUEST_METHOD

    /**
     * 요청할 target url.
     * @return 요청할 target url.
     */
    val url: String

    /**
     * http 요청에 필요한 params.
     * @return http 요청에 필요한 params.
     */
    val params: Map<String, String>

    /**
     * http 요청에 필요한 headers.
     * @return http 요청에 필요한 headers.
     */
    val headers: Map<String, String>


}
