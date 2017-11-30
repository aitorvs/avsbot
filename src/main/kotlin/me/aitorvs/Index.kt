package me.aitorvs

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.ws.rs.core.MediaType

class Index : HttpServlet() {
    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        println("Opening index")
        val html= """
            <html>
                <head>
                    <title>AVS twitter bot</title>
                </head>
                <body>
                    <h1>Demo AVS Twitter bot!</h1>
                </body>
            </html>""".trimIndent()

        resp?.apply {
            contentType = MediaType.TEXT_HTML
            resp.writer.apply {
                println(html)
                flush()
                close()
            }
        }

    }
}