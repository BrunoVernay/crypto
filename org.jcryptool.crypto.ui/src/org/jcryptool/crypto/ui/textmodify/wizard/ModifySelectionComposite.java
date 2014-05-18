// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.ui.textmodify.wizard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.jcryptool.core.logging.dialogs.JCTMessageDialog;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.alphabets.AlphabetsManager;
import org.jcryptool.core.operations.editors.EditorsManager;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.crypto.ui.CryptoUIPlugin;
import org.jcryptool.crypto.ui.alphabets.AlphabetSelectorComposite;
import org.jcryptool.crypto.ui.alphabets.AlphabetSelectorComposite.Mode;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free for non-commercial
 * use. If Jigloo is being used commercially (ie, by a corporation, company or business for any purpose whatever) then
 * you should purchase a license for each developer using Jigloo. Please visit www.cloudgarden.com for details. Use of
 * Jigloo implies acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO
 * JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
/**
 * @author Simon
 *
 */

public class ModifySelectionComposite extends Composite implements Listener {

    private Composite alphabetGroup;
    private Button alphabetYESNO;
    private Composite uppercaseGroup;
    private Button uppercaseYESNO;
    private Button uppercase;
    private Button lowercase;
    private Composite umlautGroup;
    private Button umlautYESNO;
    private Composite leerGroup;
    private Button leerYESNO;
    private Composite tryComposite;
    private Button tryButton;

    /** The operation */
    private boolean doUppercase = true;

    /** The filter state */
    private boolean uppercaseTransformationOn;
    private boolean alphabetTransformationON;
    private boolean umlautTransformationON;
    private boolean leerTransformationON;

//    private static Mode defaultMode = Mode.SINGLE_COMBO_BOX_WITH_CUSTOM_ALPHABETS;
    private static Mode defaultMode = Mode.COMBO_BOX_WITH_CUSTOM_ALPHABET_BUTTON;

    private String tryString;
    private PreviewViewer myExampleViewer;
	private Mode customAlphabetMode = defaultMode;
	private AlphabetSelectorComposite alphabetComboNew;


    /**
     * @param parent the parent composite
     * @param style SWT style for the composite
     */
    public ModifySelectionComposite(Composite parent, int style) {
        this(parent, style, new TransformData(), defaultMode);
    }

    /**
     * @param parent the parent composite
     * @param style SWT style for the composite
     * @param defaultData defines how the page's elements will be selected first
     */
    public ModifySelectionComposite(Composite parent, int style, TransformData defaultData, AlphabetSelectorComposite.Mode customAlphaMode) {
        super(parent, style);

        this.customAlphabetMode = customAlphaMode;

        GridLayout layout = new GridLayout();
        this.setLayout(layout);

        try {
            createUppercaseGroup(this);
            createUmlautGroup(this);
            createLeerGroup(this);
            createAlphabetGroup(this);
            createTryComposite(this);
        } catch (Exception e) {
            LogUtil.logError(CryptoUIPlugin.PLUGIN_ID, e);
        }

        setTransformData(defaultData);
    }

    public ModifySelectionComposite(Group transformationGroup, int style,
			TransformData firstFormSetting) {
		this(transformationGroup, style, firstFormSetting, defaultMode);
	}

	public TransformData getTransformData() {
    	//TODO: !provisory getNameForAlphabet
        return new TransformData(getSelectedFilterAlphabet(),
        		doUppercase,
        		uppercaseTransformationOn,
        		leerTransformationON,
                alphabetTransformationON,
                umlautTransformationON);
    }

    //TODO: !relocate
    /**
     * retrieves a name for a given alphabet object that is supposed to be in the JCT alphabets store.
     *
     * @param a the alphabet object
     * @return the name, or null, if not found
     */
    public static String getNameForAlphabet(AbstractAlphabet a) {
    	for(AbstractAlphabet alpha: AlphabetsManager.getInstance().getAlphabets()) {
    		if(a!=null && (a==alpha || a.equals(alpha))) {
    			return alpha.getName();
    		}
    	}
    	return null;
    }

    public void setTransformData(TransformData data) {
    	alphabetYESNO.setSelection(data.isAlphabetTransformationON());
        alphabetTransformationON = data.isAlphabetTransformationON();
        uppercaseYESNO.setSelection(data.isUppercaseTransformationOn());
        uppercaseTransformationOn = data.isUppercaseTransformationOn();
        uppercase.setSelection(data.isDoUppercase());
        lowercase.setSelection(!data.isDoUppercase());
        doUppercase = data.isDoUppercase();
        leerYESNO.setSelection(data.isLeerTransformationON());
        leerTransformationON = data.isLeerTransformationON();
        umlautYESNO.setSelection(data.isUmlautTransformationON());
        umlautTransformationON = data.isUmlautTransformationON();
        initAlphabetComposites(data.getSelectedAlphabet());

        uppercase.setEnabled(uppercaseTransformationOn);
        lowercase.setEnabled(uppercaseTransformationOn);
        alphabetComboNew.setEnabled(alphabetYESNO.getSelection());
    }

	/**
     * Initializes the alphabet composites. An empty string leads to the selection of the first alphabet
     */
    private void initAlphabetComposites(AbstractAlphabet selectAlphabet) {
        alphabetComboNew.getAlphabetInput().writeContent(selectAlphabet);
        alphabetComboNew.getAlphabetInput().synchronizeWithUserSide();
    }

    public AbstractAlphabet getSelectedFilterAlphabet() {
    	return alphabetComboNew.getAlphabetInput().getContent();
    }

    /**
     * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
     */
    @Override
	public final void handleEvent(final Event event) {
        if (event.widget == uppercase) {
            doUppercase = uppercase.getSelection();
        } else if (event.widget == lowercase) {
            doUppercase = uppercase.getSelection();
        } else if (event.widget == uppercaseYESNO) {
            uppercaseTransformationOn = uppercaseYESNO.getSelection();
            uppercase.setEnabled(uppercaseTransformationOn);
            lowercase.setEnabled(uppercaseTransformationOn);
        } else if (event.widget == alphabetYESNO) {
            alphabetTransformationON = alphabetYESNO.getSelection();
            alphabetComboNew.setEnabled(alphabetYESNO.getSelection());
        } else if (event.widget == umlautYESNO) {
            umlautTransformationON = umlautYESNO.getSelection();
        } else if (event.widget == leerYESNO) {
            leerTransformationON = leerYESNO.getSelection();
        } else if (event.widget == tryButton) {
            String text = getTryString();
            if (text == null)
                JCTMessageDialog.showInfoDialog(new Status(IStatus.INFO, CryptoUIPlugin.PLUGIN_ID,
                        Messages.PreviewViewer_fileNotOpen));
            else {
                myExampleViewer = new PreviewViewer(this.getShell());
                myExampleViewer.setText(text, this.getTransformData());
                myExampleViewer.setTitle(Messages.ModifySelectionComposite_preview);
                myExampleViewer.open();
            }
        }
    }

    /**
     * This method initializes operationGroup
     *
     */
    private void createUppercaseGroup(final Composite parent) {
        GridData uppercaseButtonGridData = new GridData();
        uppercaseButtonGridData.horizontalAlignment = SWT.FILL;
        uppercaseButtonGridData.grabExcessHorizontalSpace = true;

        GridData lowercaseButtonGridData = new GridData();
        lowercaseButtonGridData.horizontalAlignment = SWT.FILL;
        lowercaseButtonGridData.grabExcessHorizontalSpace = true;

        GridLayout singleTransformationBoxLayout = new GridLayout();
        singleTransformationBoxLayout.numColumns = 1;

        GridData singleTransformationBoxGData = new GridData();
        singleTransformationBoxGData.horizontalAlignment = SWT.FILL;
        singleTransformationBoxGData.grabExcessHorizontalSpace = true;
        singleTransformationBoxGData.verticalAlignment = SWT.BEGINNING;
        singleTransformationBoxGData.grabExcessVerticalSpace = false;
        singleTransformationBoxGData.verticalIndent = 5;

        GridData singleTransformationCheckboxGData = new GridData();
        singleTransformationCheckboxGData.grabExcessHorizontalSpace = true;
        singleTransformationCheckboxGData.horizontalAlignment = SWT.FILL;
        singleTransformationCheckboxGData.grabExcessVerticalSpace = false;
        singleTransformationCheckboxGData.verticalAlignment = SWT.BEGINNING;

        GridData singleTransformationInnerBoxGData = new GridData();
        singleTransformationInnerBoxGData.horizontalAlignment = SWT.FILL;
        singleTransformationInnerBoxGData.grabExcessHorizontalSpace = true;
        singleTransformationInnerBoxGData.verticalAlignment = SWT.FILL;
        singleTransformationInnerBoxGData.grabExcessVerticalSpace = false;
        singleTransformationInnerBoxGData.horizontalIndent = 15;

        uppercaseGroup = new Composite(parent, SWT.NONE);
        uppercaseGroup.setLayoutData(singleTransformationBoxGData);
        uppercaseGroup.setLayout(singleTransformationBoxLayout);

        uppercaseYESNO = new Button(uppercaseGroup, SWT.CHECK);
        uppercaseYESNO.setText(Messages.ModifyWizardPage_upperLower);
        uppercaseYESNO.setLayoutData(singleTransformationCheckboxGData);
        uppercaseYESNO.addListener(SWT.Selection, this);

        Composite innerGroup = new Composite(uppercaseGroup, SWT.NONE);
        innerGroup.setLayoutData(singleTransformationInnerBoxGData);
        innerGroup.setLayout(new GridLayout(2, true));

        uppercase = new Button(innerGroup, SWT.RADIO);
        uppercase.setText(Messages.ModifyWizardPage_alltoupper);
        uppercase.setLayoutData(uppercaseButtonGridData);
        uppercase.addListener(SWT.Selection, this);

        lowercase = new Button(innerGroup, SWT.RADIO);
        lowercase.setText(Messages.ModifyWizardPage_alltolower);
        lowercase.setLayoutData(lowercaseButtonGridData);
        lowercase.addListener(SWT.Selection, this);
    }

    /**
     * This method initializes operationGroup
     *
     */
    private void createLeerGroup(final Composite parent) {
        GridLayout singleTransformationBoxLayout = new GridLayout();
        singleTransformationBoxLayout.numColumns = 1;

        GridData singleTransformationBoxGData = new GridData();
        singleTransformationBoxGData.horizontalAlignment = SWT.FILL;
        singleTransformationBoxGData.grabExcessHorizontalSpace = true;
        singleTransformationBoxGData.verticalAlignment = SWT.BEGINNING;
        singleTransformationBoxGData.grabExcessVerticalSpace = false;
        singleTransformationBoxGData.verticalIndent = 5;

        GridData singleTransformationCheckboxGData = new GridData();
        singleTransformationCheckboxGData.grabExcessHorizontalSpace = true;
        singleTransformationCheckboxGData.horizontalAlignment = SWT.FILL;
        singleTransformationCheckboxGData.grabExcessVerticalSpace = false;
        singleTransformationCheckboxGData.verticalAlignment = SWT.BEGINNING;

        GridData singleTransformationInnerBoxGData = new GridData();
        singleTransformationInnerBoxGData.horizontalAlignment = SWT.FILL;
        singleTransformationInnerBoxGData.grabExcessHorizontalSpace = true;
        singleTransformationInnerBoxGData.verticalAlignment = SWT.FILL;
        singleTransformationInnerBoxGData.grabExcessVerticalSpace = false;
        singleTransformationInnerBoxGData.horizontalIndent = 15;

        leerGroup = new Composite(parent, SWT.NONE);
        leerGroup.setLayoutData(singleTransformationBoxGData);
        leerGroup.setLayout(singleTransformationBoxLayout);

        leerYESNO = new Button(leerGroup, SWT.CHECK);
        leerYESNO.setText(Messages.ModifyWizardPage_replaceblanks);
        leerYESNO.setLayoutData(singleTransformationCheckboxGData);
        leerYESNO.addListener(SWT.Selection, this);
    }

    /**
     * This method initializes operationGroup
     *
     */
    private void createUmlautGroup(final Composite parent) {

        GridLayout singleTransformationBoxLayout = new GridLayout();
        singleTransformationBoxLayout.numColumns = 1;

        GridData singleTransformationBoxGData = new GridData();
        singleTransformationBoxGData.horizontalAlignment = SWT.FILL;
        singleTransformationBoxGData.grabExcessHorizontalSpace = true;
        singleTransformationBoxGData.verticalAlignment = SWT.BEGINNING;
        singleTransformationBoxGData.grabExcessVerticalSpace = false;
        singleTransformationBoxGData.verticalIndent = 5;

        GridData singleTransformationCheckboxGData = new GridData();
        singleTransformationCheckboxGData.grabExcessHorizontalSpace = true;
        singleTransformationCheckboxGData.horizontalAlignment = SWT.FILL;
        singleTransformationCheckboxGData.grabExcessVerticalSpace = false;
        singleTransformationCheckboxGData.verticalAlignment = SWT.BEGINNING;

        GridData singleTransformationInnerBoxGData = new GridData();
        singleTransformationInnerBoxGData.horizontalAlignment = SWT.FILL;
        singleTransformationInnerBoxGData.grabExcessHorizontalSpace = true;
        singleTransformationInnerBoxGData.verticalAlignment = SWT.FILL;
        singleTransformationInnerBoxGData.grabExcessVerticalSpace = false;
        singleTransformationInnerBoxGData.horizontalIndent = 15;
        umlautGroup = new Composite(parent, SWT.NONE);
        umlautGroup.setLayoutData(singleTransformationBoxGData);
        umlautGroup.setLayout(singleTransformationBoxLayout);

        umlautYESNO = new Button(umlautGroup, SWT.CHECK);
        umlautYESNO.setText(Messages.ModifyWizardPage_umlauts);
        umlautYESNO.setLayoutData(singleTransformationCheckboxGData);
        umlautYESNO.addListener(SWT.Selection, this);

    }

    /**
     * This method initializes alphabetGroup
     *
     */
    private void createAlphabetGroup(final Composite parent) {
        GridData filterComboGridData = new GridData();
        filterComboGridData.horizontalAlignment = SWT.FILL;
        filterComboGridData.grabExcessHorizontalSpace = true;

        GridLayout singleTransformationBoxLayout = new GridLayout();
        singleTransformationBoxLayout.numColumns = 1;

        GridData singleTransformationBoxGData = new GridData();
        singleTransformationBoxGData.horizontalAlignment = SWT.FILL;
        singleTransformationBoxGData.grabExcessHorizontalSpace = true;
        singleTransformationBoxGData.verticalAlignment = SWT.BEGINNING;
        singleTransformationBoxGData.grabExcessVerticalSpace = false;
        singleTransformationBoxGData.verticalIndent = 5;

        GridData singleTransformationCheckboxGData = new GridData();
        singleTransformationCheckboxGData.grabExcessHorizontalSpace = true;
        singleTransformationCheckboxGData.horizontalAlignment = SWT.FILL;
        singleTransformationCheckboxGData.grabExcessVerticalSpace = false;
        singleTransformationCheckboxGData.verticalAlignment = SWT.BEGINNING;

        GridData singleTransformationInnerBoxGData = new GridData();
        singleTransformationInnerBoxGData.horizontalAlignment = SWT.FILL;
        singleTransformationInnerBoxGData.grabExcessHorizontalSpace = true;
        singleTransformationInnerBoxGData.verticalAlignment = SWT.FILL;
        singleTransformationInnerBoxGData.grabExcessVerticalSpace = false;
        singleTransformationInnerBoxGData.horizontalIndent = 15;

        alphabetGroup = new Composite(parent, SWT.NONE);
        alphabetGroup.setLayoutData(singleTransformationBoxGData);
        alphabetGroup.setLayout(singleTransformationBoxLayout);

        alphabetYESNO = new Button(alphabetGroup, SWT.CHECK);
        alphabetYESNO.setText(Messages.ModifyWizardPage_filteralpha);
        alphabetYESNO.setLayoutData(singleTransformationCheckboxGData);
        alphabetYESNO.addListener(SWT.Selection, this);

        Composite innerGroup = new Composite(alphabetGroup, SWT.NONE);
        innerGroup.setLayoutData(singleTransformationInnerBoxGData);
        innerGroup.setLayout(new GridLayout(1, true));

        alphabetComboNew = new AlphabetSelectorComposite(innerGroup, null, customAlphabetMode);
        alphabetComboNew.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    }

    /**
     * This method initializes alphabetGroup
     *
     */
    private void createTryComposite(final Composite parent) {

        GridLayout tryCompositeGridLayout = new GridLayout();
        tryCompositeGridLayout.numColumns = 1;

        GridData tryCompositeGridData = new GridData();
        tryCompositeGridData.horizontalAlignment = SWT.FILL;
        tryCompositeGridData.grabExcessHorizontalSpace = true;
        tryCompositeGridData.grabExcessVerticalSpace = false;
        tryCompositeGridData.verticalAlignment = SWT.BEGINNING;
        tryCompositeGridData.verticalIndent = 7;

        tryComposite = new Composite(parent, SWT.NONE);
        tryComposite.setLayoutData(tryCompositeGridData);
        tryComposite.setLayout(tryCompositeGridLayout);

        GridData tryButtonGridData = new GridData();
        tryButtonGridData.horizontalSpan = 2;
        tryButtonGridData.verticalAlignment = SWT.CENTER;
        tryButtonGridData.grabExcessHorizontalSpace = true;
        tryButtonGridData.grabExcessVerticalSpace = false;
        tryButtonGridData.horizontalAlignment = SWT.RIGHT;

        tryButton = new Button(tryComposite, SWT.PUSH);
        tryButton.setText(Messages.ModifySelectionComposite_howwillitlooklike);
        tryButton.setLayoutData(tryButtonGridData);
        tryButton.addListener(SWT.Selection, this);
    }

    /**
     * reads the current value from an input stream
     *
     * @param in the input stream
     */
    private String InputStreamToString(InputStream in) {
        if (in == null)
            return null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in, IConstants.UTF8_ENCODING));
        } catch (UnsupportedEncodingException e1) {
            LogUtil.logError(CryptoUIPlugin.PLUGIN_ID, e1);
        }

        StringBuffer myStrBuf = new StringBuffer();
        int charOut = 0;
        String output = ""; //$NON-NLS-1$
        try {
            while ((charOut = reader.read()) != -1) {
                myStrBuf.append(String.valueOf((char) charOut));
            }
        } catch (IOException e) {
            LogUtil.logError(CryptoUIPlugin.PLUGIN_ID, e);
        }
        output = myStrBuf.toString();
        return output;
    }

    /**
     * get the text from an opened editor
     */
    private String getEditorText() {
        InputStream stream = EditorsManager.getInstance().getActiveEditorContentInputStream();
        return InputStreamToString(stream);
    }

    /**
     * Sets the example String which will be used in the "Preview transformation"
     *
     * @param pTryString the example text
     */
    public void setTryString(String pTryString) {
        tryString = pTryString;
    }

    public String getTryString() {
        tryString = getEditorText();
        return tryString;
    }

}
