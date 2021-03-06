/**
 * 功能描述 
 * 文件名 ArtcleController.java
 * 作者 周泰斗
 * 编写日期 2020年6月15日下午11:02:48
 **/
package com.zy.blog.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zy.blog.annotation.DisableAuth;
import com.zy.blog.common.Response;
import com.zy.blog.controller.param.ArticleReq;
import com.zy.blog.controller.param.QueryArtcleByCategoryReq;
import com.zy.blog.controller.param.QueryArtcleByTagReq;
import com.zy.blog.controller.param.QueryArtcleReq;
import com.zy.blog.controller.param.Request;
import com.zy.blog.entity.ArticleDO;
import com.zy.blog.entity.ArticleModel;
import com.zy.blog.entity.QueryArtcleRes;
import com.zy.blog.service.IArticleService;
import com.zy.blog.util.ResponseUtil;

/**
 * 文章控制层
 * 
 * @author 周泰斗
 * @version 1.0.0.0
 */
@RestController
public class ArticleController {

	@Autowired
	private IArticleService articleService;

	/**
	 * 新建一篇文章
	 */
	@PostMapping(value = "/artcle")
	public Response addArtcle(@RequestBody @Valid ArticleReq body) {
		ArticleModel addArtcle = articleService.addArtcle(body.getUserUid(), body.getTitle(), body.getMdContent(),
				body.getTags(), body.getUrl(),
				body.getCategory(), body.getOpen(), body.getOriginal());
		return ResponseUtil.success(addArtcle);
	}

	/**
	 * 获取最新文章/热度最高/评论最多
	 */
	@DisableAuth
	@GetMapping(value = "/artcle")
	public Response queryArtcle(QueryArtcleReq req) {
		List<ArticleDO> artcles = articleService.queryArtcle(req.getMode(), req.getPageIndex());
		int count = articleService.queryArtcleCount();
		QueryArtcleRes res = new QueryArtcleRes();
		res.setArtcles(artcles);
		res.setCount(count);
		return ResponseUtil.success(res);
	}

	/**
	 * 根据Id获取文章详情
	 */
	@GetMapping(value = "/artcle/{articleId}")
	@DisableAuth
	public Response queryArtcleById(@PathVariable long articleId, @RequestBody Request req) {
		ArticleModel artcles = articleService.queryArtcleById(articleId, req.getUserUid());
		return ResponseUtil.success(artcles);
	}

	/**
	 * 根据某一标签获取相应的文章
	 */
	@DisableAuth
	@GetMapping(value = "/artcle/tags")
	public Response queryArtcleByTag(@RequestBody QueryArtcleByTagReq req) {
		List<ArticleDO> artcles = articleService.queryArtcleByTag(req.getTagName(),req.getPageIndex());
		int count = articleService.queryArtcleCountByTag(req.getTagName());
		QueryArtcleRes res = new QueryArtcleRes();
		res.setArtcles(artcles);
		res.setCount(count);
		return ResponseUtil.success(res);
	}

	/**
	 * 根据某一分类获取相应的文章
	 */
	@DisableAuth
	@GetMapping(value = "/artcle/categorys")
	public Response queryArtcleByCategory(@RequestBody QueryArtcleByCategoryReq req) {
		List<ArticleDO> artcles = articleService.queryArtcleByCategory(req.getCategoryName(),req.getPageIndex());
		int count = articleService.queryArtcleCountByCategory(req.getCategoryName());
		QueryArtcleRes res = new QueryArtcleRes();
		res.setArtcles(artcles);
		res.setCount(count);
		return ResponseUtil.success(res);
	}

	/**
	 * 更新文章
	 */
	@PutMapping(value = "/artcle/{articleId}")
	public Response update(@PathVariable long articleId, @RequestBody ArticleReq req) {
		ArticleModel model = articleService.updateArtcle(articleId, req);
		return ResponseUtil.success(model);
	}

	/**
	 * 删除一篇文章
	 */
	@DeleteMapping(value = "/artcle/{articleId}")
	public Response deleteArtcle(@PathVariable long articleId, @RequestBody Request req) {
		articleService.deleteArtcle(articleId);
		return ResponseUtil.success();
	}
}
