package com.example.postfeed.model

data class PostResponseModel(val status: String,
                             val data: List<PostListData>,
                             val totalCount: Long,
                             val currentPage: Long,
                             val message: String,)

data class PostListData(
    var post: Post?,
    val sponsor: List<Sponsor>?,
)

data class Post(
    val location: Location,
    val _id: String,
    val userId: UserId,
    val RepostPostId: Any?,
    val description: String,
    val settingRepost: Boolean,
    val media: List<Media>,
    val privacy: String,
    val group: List<Group>,
    val circleId: String,
    val postType: String,
    val hideLikeCount: Boolean,
    val turnOffComment: Boolean,
    val isPin: Boolean,
    val canReply: Boolean,
    val canShareCirclePost: Boolean,
    val countRepost: Long,
    val hashTag: List<Any?>,
    val allowDownloadPost: Boolean,
    val createAt: String,
    val likeUser: List<Any?>,
    var TotalLike: Long,
    var selfLike: Boolean,
    val shareCount: Long,
    val comments: Long,
    val savePost: Boolean,
)


data class Location(
    val name: String,
    val longitude: Any?,
    val latitude: Any?,
)

data class UserId(
    val _id: String,
    val name: String,
    val email: String,
    val phone: Long,
    val profile: String,
    val userType: String,
    val contactStatus: Boolean,
    val webName: String,
)

data class Media(
    val url: String,
    val aspectRatio: Float,
    val type: String,
    val thumbnail: String,
    val duration: String,
    val size: Long,
    val mediaTagUser: List<Any?>,
    val _id: String,
)

data class Group(
    val GroupId: GroupId,
)

data class GroupId(
    val _id: String,
    val groupName: String,
    val groupProfile: String,
    val groupType: String,
    val isDeleted: Boolean,
    val storyCount: Long,
    val isSeenStory: Boolean,
    val isJoined: Boolean,
    val userRole: String,
)

data class Sponsor(
    val _id: String,
    val groupName: String,
    val groupProfile: String,
    val groupDescription: String,
    val pendingMembers: List<Any?>,
    val memberLength: Long,
    val storyCount: Long,
    val isSeenStory: Boolean,
)

