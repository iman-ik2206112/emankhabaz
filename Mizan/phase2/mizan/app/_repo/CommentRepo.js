import userRepo from "./UserRepo";
import prisma from "@/lib/prisma";

class CommentRepo {

  async #readComments() {
    const comments = await prisma.comment.findMany({
      orderBy: {
        createdDate: "asc",
      },
    });
    return comments
  }

  async #writeComments(comments) {
    await prisma.comment.create({data:comments})
  }

  async getComments(sectionCRN) {
    //filter by sectionCRN, then return comment with author fist and last name
    const sectionComments= await prisma.comment.findMany({
      where:{sectionCRN},include:{user:true}
    })
    const commentsWithAuthor=sectionComments.map(
      (comment)=> ({
        ...comment,
        createdDate:comment.createdDate.toISOString().split("T")[0],
        authorName:`${comment.user.firstName} ${comment.user.lastName}`})
    )
    console.log("Comments with author:", commentsWithAuthor);
    return commentsWithAuthor;
  }

  async getCommentReplies(commentId) {
    const replies= await prisma.comment.findMany({
      where:{replyToCommentId:commentId},include:{user:true}
    })
    const repliesWithAuthor=replies.map(
      (comment)=> ({...comment,createdDate:comment.createdDate.toISOString().split("T")[0],authorName:`${comment.user.firstName} ${comment.user.lastName}`})
    )
    return repliesWithAuthor;
  }

  async addComment(comment) {
    const newComment = {
      ...comment,
      createdDate: new Date()
    };
    //excluding authorId and sectionCRN from data in prisma create
    const {authorId,sectionCRN,...commentData}=newComment 
    
    return await prisma.comment.create({
      data: {
        ...commentData,
        user: { connect: { id: authorId } },
        sections: {connect:{crn:sectionCRN}}
      },
    });
  }

  async deleteComment(commentId) {
    const newID = parseInt(commentId)
    await prisma.comment.deleteMany({where:{OR:[{id:newID},{replyToCommentId:newID}]}})
  }
}

export default new CommentRepo();
