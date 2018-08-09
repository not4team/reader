package com.book.novel.model.remote

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * Created by newbiechen on 17-4-20.
 */

interface BookApi {

    @GET("https://m.qidian.com/{gender}")
    fun bookCity(@Path("gender") gender: String): Single<ResponseBody>

    /**
     * 获取所有排行榜
     *
     * @return
     */
    @GET("https://m.qidian.com/rank/{rankName}/{gender}")
    fun rank(@Path("rankName") rankName: String, @Path("gender") gender: String): Single<ResponseBody>

    @GET("https://m.qidian.com/rank/{rankName}/{gender}")
    fun rankCategory(@Path("rankName") rankName: String, @Path("gender") gender: String, @Query("gender") gender1: String, @Query("catId") catId: String): Single<ResponseBody>

    //https://m.qidian.com/majax/rank/hotsaleslist?_csrfToken=CjcfEH4J9OnJNonoMfMsA8T5cQ8RxGjmdFy7WuXq&gender=male&pageNum=4&catId=-1
    @GET("https://m.qidian.com/majax/rank/{rankName}list")
    fun rankCategoryPage(@Path("rankName") rankName: String, @Query("gender") gender1: String, @Query("catId") catId: String, @Query("pageNum") pageNum: Int): Single<ResponseBody>

    /**
     * 获取书籍的章节总列表
     *
     * @param bookId
     * @param view   默认参数为:chapters
     * @return
     */
    @GET
    fun getBookChapterPackage(@Url url: String): Single<ResponseBody>

    /**
     * 章节的内容
     * 这里采用的是同步请求。
     *
     * @param url
     * @return
     */
    @GET
    fun getChapterInfoPackage(@Url url: String): Single<ResponseBody>

    /**
     * 书籍详情
     *
     * @param bookId
     * @return
     */
    @GET
    fun getBookDetail(@Url url: String): Single<ResponseBody>

    /**
     * 书籍详情-你可能感兴趣的书籍
     *
     * @param bookId
     * @return
     */
    @GET("/book/{bookId}/recommend")
    fun getBookDetailRecommend(@Path("bookId") bookId: String): Single<ResponseBody>

    /**
     * 书籍查询
     *
     * @param query:作者名或者书名
     * @return
     */
    @GET
    fun getSearchBookPackage(@Url url: String): Single<ResponseBody>

    @FormUrlEncoded
    @POST
    fun getSearchBookPackagePost(@Url url: String, @FieldMap(encoded = true) params: Map<String, String>): Single<ResponseBody>
}
