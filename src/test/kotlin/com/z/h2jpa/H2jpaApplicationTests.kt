package com.z.h2jpa

import com.fasterxml.jackson.databind.ObjectMapper
import com.z.h2jpa.domain.Product
import com.z.h2jpa.domain.Provider
import com.z.h2jpa.service.ProductService
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.Assert.assertThat
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class H2jpaApplicationTests {
	@Autowired private lateinit var webApplicationContext: WebApplicationContext
	@Autowired private lateinit var mapper: ObjectMapper
	private val mock: MockMvc by lazy {
		MockMvcBuilders.webAppContextSetup(webApplicationContext).alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print()).build()
	}

	@Autowired private lateinit var productService: ProductService
	@Test
	fun a_getProductById() {
		val productId = 1
		val fromDb = productService.findById(productId)
		mock.perform(get("/product/$productId")).apply {
			andExpect(status().isOk)
			andExpect(jsonPath("$.id", `is`(fromDb?.id)))
			andExpect(jsonPath("$.name", `is`(fromDb?.name)))
			andExpect(jsonPath("$.price", `is`(fromDb?.price)))
			//andExpect(jsonPath("$.provider.name", `is`(fromDb?.provider?.name)))
			andExpect(jsonPath("$.ticketDetails").doesNotExist())
		}
	}

	@Test
	fun b_PostProduct(){
		val product = Product(name = "test", price = 20.0, stock = 5, id = 1,provider = Provider(name = "Mariano"))
		val json = mock.perform(post("/product").body(product)).apply {
			andExpect(status().isOk)
			andExpect(jsonPath("$.id", notNullValue()))
		}.toJson()
		assertThat(productService.findById(json["id"].asInt()), notNullValue())
	}

}
