package com.yomahub.liteflow.test.component;

import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import com.yomahub.liteflow.test.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.noear.solon.annotation.Import;
import org.noear.solon.annotation.Inject;
import org.noear.solon.test.SolonTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 组件功能点测试 单元测试
 *
 * @author donguo.tao
 */
@Import(profiles ="classpath:/component/application.properties")
@SolonTest(classes = FlowExecutorELDeclMultiSpringbootTest.class)
public class FlowExecutorELDeclMultiSpringbootTest extends BaseTest {

	private static final Logger LOG = LoggerFactory.getLogger(FlowExecutorELDeclMultiSpringbootTest.class);

	@Inject
	private FlowExecutor flowExecutor;

	// isAccess方法的功能测试
	@Test
	public void testIsAccess() {
		LiteflowResponse response = flowExecutor.execute2Resp("chain1", 101);
		Assertions.assertTrue(response.isSuccess());
		Assertions.assertNotNull(response.getSlot().getResponseData());
	}

	// 组件抛错的功能点测试
	@Test
	public void testComponentException() throws Exception {
		Assertions.assertThrows(ArithmeticException.class, () -> {
			LiteflowResponse response = flowExecutor.execute2Resp("chain2", 0);
			Assertions.assertFalse(response.isSuccess());
			Assertions.assertEquals("/ by zero", response.getMessage());

			if(response.getCause() != null) {
				throw response.getCause();
			}
			//ReflectionUtils.rethrowException(response.getCause());
		});
	}

	// isContinueOnError方法的功能点测试
	@Test
	public void testIsContinueOnError() throws Exception {
		LiteflowResponse response = flowExecutor.execute2Resp("chain3", 0);
		Assertions.assertTrue(response.isSuccess());
		Assertions.assertNull(response.getCause());
	}

	// isEnd方法的功能点测试
	@Test
	public void testIsEnd() throws Exception {
		LiteflowResponse response = flowExecutor.execute2Resp("chain4", 10);
		Assertions.assertTrue(response.isSuccess());
		Assertions.assertEquals("d", response.getExecuteStepStr());
	}

	// setIsEnd方法的功能点测试
	@Test
	public void testSetIsEnd1() throws Exception {
		LiteflowResponse response = flowExecutor.execute2Resp("chain5", 10);
		Assertions.assertTrue(response.isSuccess());
		Assertions.assertEquals("e", response.getExecuteStepStr());
	}

	// 条件组件的功能点测试
	@Test
	public void testNodeCondComponent() {
		LiteflowResponse response = flowExecutor.execute2Resp("chain6", 0);
		Assertions.assertTrue(response.isSuccess());
	}

	// 测试setIsEnd如果为true，continueError也为true，那不应该continue了
	@Test
	public void testSetIsEnd2() throws Exception {
		LiteflowResponse response = flowExecutor.execute2Resp("chain7", 10);
		Assertions.assertTrue(response.isSuccess());
		Assertions.assertEquals("g", response.getExecuteStepStr());
	}

}
