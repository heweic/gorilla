package org.myframe.gorilla.test.sentinel;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

public class Test {

	public static void main(String[] args) {

		List<FlowRule> rules = new ArrayList<>();
		FlowRule rule = new FlowRule();
		rule.setResource("HelloWorld");
		// set limit qps to 20
		rule.setCount(20);
		rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
		rules.add(rule);
		FlowRuleManager.loadRules(rules);

		for (int i = 0; i < 10; i++) {
			Test test = new Test();

			test.helloWorld();
		}
	}

	public void helloWorld() {
		Entry entry = null;

		try {

			entry = SphU.entry("helloWorld");

			System.out.println("hello world");
		} catch (BlockException e) {

		} finally {
			if (entry != null) {
				entry.exit();
			}
		}
	}
}
