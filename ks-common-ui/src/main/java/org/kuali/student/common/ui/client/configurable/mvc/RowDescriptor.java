package org.kuali.student.common.ui.client.configurable.mvc;

import java.util.ArrayList;
import java.util.List;

import org.kuali.student.common.ui.client.configurable.mvc.Section.FieldLabelType;
import org.kuali.student.common.ui.client.images.KSImages;
import org.kuali.student.common.ui.client.widgets.KSLabel;
import org.kuali.student.common.ui.client.widgets.KSRequiredMarker;
import org.kuali.student.common.ui.client.widgets.layout.HorizontalBlockFlowPanel;
import org.kuali.student.common.ui.client.widgets.layout.VerticalFlowPanel;
import org.kuali.student.core.validation.dto.ValidationResultInfo;
import org.kuali.student.core.validation.dto.ValidationResultInfo.ErrorLevel;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class RowDescriptor extends Composite{
    private HorizontalBlockFlowPanel rowPanel = new HorizontalBlockFlowPanel();
    private HorizontalBlockFlowPanel columnLayout =  new HorizontalBlockFlowPanel();
    private VerticalFlowPanel validationPanel = new VerticalFlowPanel();
    private List<FieldDescriptor> fields = new ArrayList<FieldDescriptor>();
    private List<Section> sections = new ArrayList<Section>();
    
    protected FieldLabelType currentFieldLabelType = FieldLabelType.LABEL_TOP;
    
    public RowDescriptor(){
    	rowPanel.addStyleName("KS-Field-Column");
    	validationPanel.addStyleName("KS-Validation-Column");
    	columnLayout.addStyleName("KS-Configurable-Row");
    	columnLayout.add(rowPanel);
    	columnLayout.add(validationPanel);
        this.initWidget(columnLayout);
    }
    
    public void addSection(Section section){
        sections.add(section);
        rowPanel.add(section);
    }
    
    public void addField(FieldDescriptor fieldDescriptor){
        fields.add(fieldDescriptor);
        if (fieldDescriptor.getFieldWidget() instanceof MultiplicityComposite){
            MultiplicityComposite listField = (MultiplicityComposite)fieldDescriptor.getFieldWidget(); 
            listField.redraw();
            rowPanel.add(listField);
        }
        else{   
            KSLabel label = new KSLabel(fieldDescriptor.getFieldLabel());
            //label.addStyleName(KSStyles.KS_FORMLAYOUT_LABEL);
            if(currentFieldLabelType == FieldLabelType.LABEL_LEFT){
                if(!(label.getText().equals("")) && label.getText() != null){
                    rowPanel.add(label);
                }
                rowPanel.add(fieldDescriptor.getFieldWidget());
            }
            else if(currentFieldLabelType == FieldLabelType.LABEL_TOP){
                if(!(label.getText().equals("")) && label.getText() != null){
                    FlowPanel vp = new FlowPanel();
                    vp.add(label);
                    vp.add(fieldDescriptor.getFieldWidget());
                    rowPanel.add(vp);
                }
                else{
                    rowPanel.add(fieldDescriptor.getFieldWidget());
                }
            }
            rowPanel.add(new KSRequiredMarker(fieldDescriptor.getRequiredState()));
        }
    }

    public void addWidget(Widget widget){
        rowPanel.add(widget);
    }
    public List<FieldDescriptor> getFields() {
        return fields;
    }

    public List<Section> getSections() {
        return sections;
    }

    public FieldLabelType getCurrentFieldLabelType() {
        return currentFieldLabelType;
    }

    public void setCurrentFieldLabelType(FieldLabelType currentFieldLabelType) {
        this.currentFieldLabelType = currentFieldLabelType;
    }   
    
    public void setValidationMessages(List<ValidationResultInfo> list){
    	for(ValidationResultInfo vr: list){
    		HorizontalBlockFlowPanel validationLine = new HorizontalBlockFlowPanel();
    		VerticalFlowPanel imagePanel = new VerticalFlowPanel();
    		KSLabel message = new KSLabel(vr.getMessage());
    		Image image;
    		message.setWordWrap(true);
    		if(vr.getLevel() == ErrorLevel.ERROR){
    			message.addStyleName("KS-Validation-Error-Message");
    			image = KSImages.INSTANCE.errorIcon().createImage();
    			
    		}
    		else if(vr.getLevel() == ErrorLevel.WARN){
    			message.addStyleName("KS-Validation-Warning-Message");
    			image = KSImages.INSTANCE.warningIcon().createImage();
    		}
    		else{
    			message.addStyleName("KS-Validation-Ok-Message");
    			image = KSImages.INSTANCE.okIcon().createImage();
    		}
    		imagePanel.add(image);
    		message.addStyleName("KS-Validation-Message");
    		validationLine.addStyleName("KS-Validation-Line");
    		image.addStyleName("KS-Validation-Image");
    		imagePanel.addStyleName("KS-Validation-Image-Panel");
    		validationLine.add(imagePanel);
    		validationLine.add(message);
    		validationPanel.add(validationLine);
        }
    }
    
    public void clearValidationMessages(){
    	validationPanel.clear();
    }
    
    public void clear(){
        clearValidationMessages();
        for (Section s:sections){
            s.clear();
        }
        for (FieldDescriptor fd:fields){
            //TODO: Only resettting multplicity composite, should approporiately reset clear all field widgets
            Widget field = fd.getFieldWidget();
            if (field instanceof MultiplicityComposite){
                ((MultiplicityComposite)field).clear();
            }
        }
    }
}