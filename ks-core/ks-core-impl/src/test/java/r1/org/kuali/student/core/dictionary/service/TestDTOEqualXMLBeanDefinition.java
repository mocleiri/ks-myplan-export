package r1.org.kuali.student.core.dictionary.service;

import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.kuali.student.r1.common.dictionary.service.impl.ComplexSubstructuresHelper;
import org.kuali.student.r2.core.atp.dto.AtpInfo;
import org.kuali.student.r2.core.comment.dto.CommentInfo;
import org.kuali.student.r2.core.comment.dto.TagInfo;
import org.kuali.student.r2.core.organization.dto.OrgInfo;
import org.kuali.student.r2.core.statement.dto.ReqCompFieldInfo;
import org.kuali.student.r2.core.statement.dto.ReqComponentInfo;
import org.kuali.student.r2.core.statement.dto.StatementInfo;
import org.kuali.student.r2.core.statement.dto.StatementTreeViewInfo;

//import org.kuali.student.r1.core.statement.dto.StatementInfo;

public class TestDTOEqualXMLBeanDefinition {

	
	
//	@Test
	public void testOrgDTOsAgainstDataDictionary() {
		System.out.println("testing statement dictionary");

		Set<String> startingClasses = new LinkedHashSet();
		startingClasses.add(OrgInfo.class.getName());
//		startingClasses.add(ReqComponentInfo.class.getName());
//		startingClasses.add(ReqCompFieldInfo.class.getName());
//		startingClasses.add(StatementTreeViewInfo.class.getName());
		String contextFile = "organization-dictionary-config";
		String outFile = "target/" + contextFile + ".txt";
		DictionaryDiscrepencyTesterHelper helper = new DictionaryDiscrepencyTesterHelper(
				outFile, startingClasses, contextFile + ".xml", false);
		helper.setPrintDescrepenciesOnly(true);
		List<String> errors = helper.doTest();
		if (errors.size() > 0) {
			fail("failed dictionary validation:\n" + formatAsString(errors));
		}

	}
	
	@Test
	public void testCommentDTOsAgainstDataDictionary() {
		System.out.println("testing statement dictionary");

		Set<String> startingClasses = new LinkedHashSet();
		startingClasses.add(CommentInfo.class.getName());
		startingClasses.add(TagInfo.class.getName());
		String contextFile = "ks-comment-dictionary-context";
		String outFile = "target/" + contextFile + ".txt";
		DictionaryDiscrepencyTesterHelper helper = new DictionaryDiscrepencyTesterHelper(
				outFile, startingClasses, contextFile + ".xml", false);
		helper.setPrintDescrepenciesOnly(true);
		List<String> errors = helper.doTest();
		if (errors.size() > 0) {
			fail("failed dictionary validation:\n" + formatAsString(errors));
		}

	}
	
	private void testDTOsAgainstDataDictionary(String contextFile,ArrayList<String> listOfDTOsToCheck) {
		System.out.println("testing statement dictionary");

		Set<String> startingClasses = new LinkedHashSet();
//		startingClasses.add(AtpInfo.class.getName());
		
		for (String className : startingClasses) {
			startingClasses.add(className);
		}
		
		
		
		String contextFilePath = contextFile;
		String outFile = "target/" + contextFile + ".txt";
		DictionaryDiscrepencyTesterHelper helper = new DictionaryDiscrepencyTesterHelper(
				outFile, startingClasses, contextFile + ".xml", false);
		helper.setPrintDescrepenciesOnly(true);
		List<String> errors = helper.doTest();
		if (errors.size() > 0) {
			fail("failed dictionary validation:\n" + formatAsString(errors));
		}

	}


	private String formatAsString(List<String> errors) {
		int i = 0;
		StringBuilder builder = new StringBuilder();
		for (String error : errors) {
			i++;
			builder.append(i + ". " + error + "\n");
		}
		return builder.toString();
	}

	// @Test
	public void getLinkedListFields() {
		ComplexSubstructuresHelper helpme = new ComplexSubstructuresHelper();
		ArrayList<Field> fields = new ArrayList<Field>();
		fields = helpme.getAllFields(fields, StatementInfo.class);
		for (Field field : fields) {
			System.out.println(field.getName());
		}
	}

//	@Test
	public void testStatementDTOsAgainstDataDictionary() {
		System.out.println("testing statement dictionary");

		Set<String> startingClasses = new LinkedHashSet();
		startingClasses.add(StatementInfo.class.getName());
		startingClasses.add(ReqComponentInfo.class.getName());
		startingClasses.add(ReqCompFieldInfo.class.getName());
		startingClasses.add(StatementTreeViewInfo.class.getName());
		String contextFile = "ks-statement-dictionary-context";
		String outFile = "target/" + contextFile + ".txt";
		DictionaryDiscrepencyTesterHelper helper = new DictionaryDiscrepencyTesterHelper(
				outFile, startingClasses, contextFile + ".xml", false);
		helper.setPrintDescrepenciesOnly(true);
		List<String> errors = helper.doTest();
		if (errors.size() > 0) {
			fail("failed dictionary validation:\n" + formatAsString(errors));
		}

	}
}
