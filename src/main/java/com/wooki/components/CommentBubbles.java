//
// Copyright 2009 Robin Komiwes, Bruno Verachten, Christophe Cordenier
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// 	http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.wooki.components;

import java.util.List;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;

import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.biz.CommentManager;

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

	@Parameter
	private Long bookId;
	
	@Inject
	private ComponentResources resources;

	@Inject
	private RenderSupport support;

	@Inject
	private ChapterManager chapterManager;

	@Inject
	private CommentManager commentManager;

	@InjectComponent
	private Zone commentList;

	@InjectComponent
	private Dialog commentDialog;

	@Inject
	private Block commentsBlock;

	@Property
	private List<Object[]> commentsInfos;

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
		this.publicationId = publicationId;
		this.domId = domId;
		return commentsBlock;
	}

	@OnEvent(value = "updateBubbles")
	public JSONObject getCommentsInfos(Long publicationId) {
		this.commentsInfos = this.commentManager
				.listCommentInfos(publicationId);
		JSONObject data = new JSONObject();
		if (this.commentsInfos != null) {
			for (Object[] obj : this.commentsInfos) {
				data.put((String) obj[0], ((Long) (obj[1])).toString());
			}
		}
		return data;
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
		bubble.put("updateUrl", resources.createEventLink("updateBubbles", this.publicationId).toAbsoluteURI());
		if (this.commentsInfos != null) {
			for (Object[] obj : this.commentsInfos) {
				bubble.put((String) obj[0], ((Long) (obj[1])).toString());
			}
		}

		support.addInit("initBubbles", bubble);
	}

}
