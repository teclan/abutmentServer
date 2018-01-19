package by.ywzn.abutmentServer.job;

import javax.annotation.Resource;

import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import by.ywzn.abutmentServer.dao.AbutmentDao;

@Component
public class ScheduledTask {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ScheduledTask.class);

	@Resource(name = "abutmentDao")
	private AbutmentDao abutmentDao;

	// 每天检查一次，一天共有 86400000 毫秒
	@Scheduled(fixedRate = 86400000)
	public void cleanForwardHistory() {
		LOGGER.info("开始清理转发历史记录....");
		abutmentDao.cleanForwardHistory();
	}

}
