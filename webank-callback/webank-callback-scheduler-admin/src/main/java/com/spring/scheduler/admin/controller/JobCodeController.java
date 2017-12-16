package com.spring.scheduler.admin.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.scheduler.admin.core.model.JobInfo;
import com.spring.scheduler.admin.core.model.JobLogGlue;
import com.spring.scheduler.admin.dao.JobInfoDao;
import com.spring.scheduler.admin.dao.JobLogGlueDao;
import com.spring.scheduler.core.biz.model.ReturnT;
import com.spring.scheduler.core.glue.GlueTypeEnum;

/**
 * ClassName: JobCodeController 
 * @Description: 
 * @author JornTang
 * @email 957707261@qq.com
 * @date 2017年8月17日
 */
@Controller
@RequestMapping("/jobcode")
public class JobCodeController {
	
	@Resource
	private JobInfoDao jobInfoDao;
	@Resource
	private JobLogGlueDao jobLogGlueDao;

	@RequestMapping
	public String index(Model model, int jobId) {
		JobInfo jobInfo = jobInfoDao.loadById(jobId);
		List<JobLogGlue> jobLogGlues = jobLogGlueDao.findByJobId(jobId);

		if (jobInfo == null) {
			throw new RuntimeException("抱歉，任务不存在");
		}
		if (GlueTypeEnum.BEAN == GlueTypeEnum.match(jobInfo.getGlueType())) {
			throw new RuntimeException("该任务非GLUE模式.");
		}

		// Glue����-�ֵ�
		model.addAttribute("GlueTypeEnum", GlueTypeEnum.values());

		model.addAttribute("jobInfo", jobInfo);
		model.addAttribute("jobLogGlues", jobLogGlues);
		return "jobcode/jobcode.index";
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public ReturnT<String> save(Model model, int id, String glueSource, String glueRemark) {
		// valid
		if (glueRemark==null) {
			return new ReturnT<String>(500, "请输入备注");
		}
		if (glueRemark.length()<4 || glueRemark.length()>100) {
			return new ReturnT<String>(500, "备注长度应该在4至100之间");
		}
		JobInfo exists_jobInfo = jobInfoDao.loadById(id);
		if (exists_jobInfo == null) {
			return new ReturnT<String>(500, "参数异常");
		}
		
		// update new code
		exists_jobInfo.setGlueSource(glueSource);
		exists_jobInfo.setGlueRemark(glueRemark);
		exists_jobInfo.setGlueUpdatetime(new Date());
		jobInfoDao.update(exists_jobInfo);

		// log old code
		JobLogGlue jobLogGlue = new JobLogGlue();
		jobLogGlue.setJobId(exists_jobInfo.getId());
		jobLogGlue.setGlueType(exists_jobInfo.getGlueType());
		jobLogGlue.setGlueSource(glueSource);
		jobLogGlue.setGlueRemark(glueRemark);
		jobLogGlueDao.save(jobLogGlue);

		// remove code backup more than 30
		jobLogGlueDao.removeOld(exists_jobInfo.getId(), 30);

		return ReturnT.SUCCESS;
	}
	
}
