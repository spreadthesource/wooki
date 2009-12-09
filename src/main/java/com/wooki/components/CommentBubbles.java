package com.wooki.components;

import java.util.List;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.beaneditor.Width;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;

import com.wooki.WookiEventConstants;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.biz.CommentManager;
import com.wooki.domain.model.Comment;

/**
 * This component can be us in page that display a chapter publication to create
 * comment bubbles and enable comment creation.
 * 
 * @author ccordenier
 * 
 */
public class CommentBubbles {

	@Parameter
	private Long publicationId;

	@Inject
	private ComponentResources resources;

	@Inject
	private RenderSupport support;

	@Inject
	private ChapterManager chapterManager;

	@Inject
	private CommentManager commentManager;

	@InjectComponent
	private Form createCommentForm;

	@InjectComponent
	private Zone commentList;

	@InjectComponent
	private CommentDialog commentDialog;

	@Inject
	private Block commentsBlock;

	@Inject
	private Block commentDetails;

	@Property
	private List<Object[]> commentsInfos;

	@Property
	private List<Comment> comments;

	@Property
	private Comment currentComment;

	@Property
	@Validate("required")
	@Width(80)
	private String content;

	@Persist
	@Property
	private String domId;

	@SetupRender
	public Object searchComments() {
		if (!resources.isBound("publicationId") || publicationId == null) {
			return false;
		}
		this.commentsInfos = this.commentManager
				.listCommentInfos(publicationId);
		return true;
	}

	@OnEvent(value = "displayComment")
	public Object commentsDisplay(Long publicationId, String domId) {
		this.domId = domId;
		this.comments = commentManager.listOpenForPublicationAndDomId(
				publicationId, domId);
		createCommentForm.clearErrors();
		return commentsBlock;
	}

	@OnEvent(value = EventConstants.SUCCESS, component = "createCommentForm")
	public Object addComment() {
		this.currentComment = chapterManager.addComment(publicationId, content,
				this.domId);
		this.comments = commentManager.listOpenForPublicationAndDomId(
				publicationId, domId);
		return this.commentDetails;
	}

	@OnEvent(value = WookiEventConstants.REMOVE)
	public void removeComment(Long comId) {
		this.commentManager.removeComment(comId);
	}

	@AfterRender
	void addScript() {

		JSONObject bubble = new JSONObject();

		// Add update link
		Link lnk = resources.createEventLink("displayComment",
				this.publicationId, "blockId");
		bubble.put("url", lnk.toRedirectURI());
		bubble.put("zoneId", commentList.getClientId());
		bubble.put("dialogId", commentDialog.getClientId());
		if (this.commentsInfos != null) {
			for (Object[] obj : this.commentsInfos) {
				bubble.put((String) obj[0], ((Long) (obj[1])).toString());
			}
		}

		support.addInit("initBubbles", bubble);
	}

}
