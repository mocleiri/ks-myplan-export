package org.kuali.student.core.comment;

import org.kuali.student.common.util.constants.CommonServiceConstants;
import org.kuali.student.core.comment.dto.CommentInfo;
import org.kuali.student.core.comment.dto.TagInfo;
import org.kuali.student.common.util.constants.CommonServiceConstants  ;
/**
 * This class holds the constants used by the Comment service
 *
 * @Version 2.0
 * @Author Sri komandur@uw.edu
 *
 */
public class CommentServiceConstants {
    /**
     * Reference Object URI's
     */
    public static final String NAMESPACE = CommonServiceConstants.REF_OBJECT_URI_GLOBAL_PREFIX + "comment";
    public static final String REF_OBJECT_URI_COMMENT = NAMESPACE + "/" + CommentInfo.class.getSimpleName();
    public static final String REF_OBJECT_URI_TAG = NAMESPACE + "/" + TagInfo.class.getSimpleName();
}
