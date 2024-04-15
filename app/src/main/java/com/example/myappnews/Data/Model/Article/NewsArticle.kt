package com.example.myappnews.Data.Model.Article

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class NewsArticle(
    var idArticle: String? = null,//id bài báo
    var idPoster:String?=null,//id người đăng
    var idReviewer:String?=null,//id người xét duyệt
    var titleArticle: String? = null,//tiêu đề bài báo
    var linkArticle: String? = null,//link bài báo
    var creator: String? = null,//tên tác giả
    var content: String? = null,//nội dung bài báo
    var pubDate: Date? = null,//ngày xuất bản
    var imageUrl: String? = null,// đường dẫn ảnh
    var sourceUrl: String? = null,// đường dẫn đến trang
    var sourceId: String? = null,//tên trang web
    var country: String? = null,//đất  nước
    var field: String? = null,//lĩnh vực trong xã hội
    var isApprove: Int? = 0,// đã đăng
    var hide: Boolean? = true,//ẩn hiện
    var requireEdit:Int?=null,
    var requiredDate:Date?=null,// thời gian yêu cầu xét duyệt
    var cause: String? = null,//lý do
) : Parcelable {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "idArticle" to idArticle,
            "idPoster" to idPoster,
            "titleArticle" to titleArticle,
            "idReviewer" to idReviewer,
            "linkArticle" to linkArticle,
            "creator" to creator,
            "content" to content,
            "pubDate" to pubDate,
            "imageUrl" to imageUrl,
            "sourceUrl" to sourceUrl,
            "sourceId" to sourceId,
            "country" to country,
            "field" to field,
            "isApprove" to isApprove,
            "hide" to hide,
            "requireEdit" to requireEdit,
            "requiredDate" to requiredDate,
            "cause" to cause
        )
    }
}
