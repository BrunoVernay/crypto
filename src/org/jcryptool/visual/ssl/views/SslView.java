package org.jcryptool.visual.ssl.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.ssl.protocol.Message;

/**
 * Represents the visual TLS-Plugin.
 * 
 * @author Florian Stehrer
 */
public class SslView extends ViewPart
{
	private Composite parent;
	private ScrolledComposite scrolledComposite;
	private Arrows arrow;
	private Composite content;
	private Composite mainContent;
	private ClientHelloComposite clientHelloComposite;
	private ServerHelloComposite serverHelloComposite;
	private ServerCertificateComposite serverCertificateComposite;
	private ClientCertificateComposite clientCertificateComposite;
	private ServerChangeCipherSpecComposite serverChangeCipherSpecComposite;
	private ClientChangeCipherSpecComposite clientChangeCipherSpecComposite;
	private ClientFinishedComposite clientFinishedComposite;
	private ServerFinishedComposite serverFinishedComposite;
	private StyledText stxInformation;
	private Group grp_serverComposites;
	private Group grp_clientComposites;
	private Button btnNextStep;
	private Button btnPreviousStep;
	
	public static final String ID = "org.jcryptool.visual.ssl.views.SslView"; //$NON-NLS-1$
	
	public SslView() {
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(final Composite parent) 
	{
		this.parent = parent;
		scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		mainContent = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(mainContent);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setMinSize(mainContent.computeSize(1100, 900));
		
		GridLayout gl = new GridLayout(1, false);
        gl.verticalSpacing = 0;
        mainContent.setLayout(gl);
        
        Label headline = new Label(mainContent, SWT.NONE);
      	headline.setText(Messages.SslViewHeadline);
      	headline.setFont(FontService.getHeaderFont());
      	headline.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
      	
        content = new Composite(mainContent, SWT.NONE);
        content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        content.setLayout(new GridLayout(4, false));
        
        createButtons();
		createGui();
		
		//Fuer die Hilfe:
		//PlatformUI.getWorkbench().getHelpSystem().setHelp(parent.getShell(), SslPlugin.PLUGIN_ID + ".sslview");
	}
	
	/**
	 * Creates the Elements of the GUI.
	 */
	private void createGui()
	{
		//Client Composites
		grp_clientComposites = new Group(content, SWT.NONE);
		GridData gd_clientComposite = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_clientComposite.widthHint = 350;
		grp_clientComposites.setLayoutData(gd_clientComposite);
		grp_clientComposites.setLayout(new GridLayout());
		grp_clientComposites.setText(Messages.SslViewLblClient);
		
		clientHelloComposite = new ClientHelloComposite(grp_clientComposites, SWT.NONE, this);
		clientHelloComposite.setLayout(new GridLayout(3, true));
		clientHelloComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		clientCertificateComposite = new ClientCertificateComposite(grp_clientComposites, SWT.NONE, this);
		clientCertificateComposite.setLayout(new GridLayout(3, true));
		clientCertificateComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2));
		clientCertificateComposite.setVisible(false);
		
		clientChangeCipherSpecComposite = new ClientChangeCipherSpecComposite(grp_clientComposites, SWT.NONE, this);
		clientChangeCipherSpecComposite.setLayout(new GridLayout(3, true));
		clientChangeCipherSpecComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 3));
		clientChangeCipherSpecComposite.setVisible(false);
		
		clientFinishedComposite = new ClientFinishedComposite(grp_clientComposites, SWT.NONE, this);
		clientFinishedComposite.setLayout(new GridLayout(3, true));
		clientFinishedComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 4));
		clientFinishedComposite.setVisible(false);
		
		
		//Draw Panel
		Composite swtAwtComponent = new Composite(content, SWT.EMBEDDED);
		GridData gd_drawPanel = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 2);
		gd_drawPanel.widthHint = 100;
		swtAwtComponent.setLayoutData(gd_drawPanel);
		swtAwtComponent.setLayout(new GridLayout());
		java.awt.Frame frame = SWT_AWT.new_Frame(swtAwtComponent);
		arrow = new Arrows();
		frame.add(arrow);
		
		
		//Server Composites
		grp_serverComposites = new Group(content, SWT.NONE);
		GridData gd_serverComposite = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 3);
		gd_serverComposite.widthHint = 350;
		grp_serverComposites.setLayoutData(gd_serverComposite);
		grp_serverComposites.setLayout(new GridLayout());
		grp_serverComposites.setText(Messages.SslViewLblServer);
		
		serverHelloComposite = new ServerHelloComposite(grp_serverComposites, SWT.NONE, this);
		serverHelloComposite.setLayout(new GridLayout(3, true));
		serverHelloComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		serverHelloComposite.setVisible(false);
		
		serverCertificateComposite = new ServerCertificateComposite(grp_serverComposites, SWT.NONE, this);
		serverCertificateComposite.setLayout(new GridLayout(3, true));
		serverCertificateComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2));
		serverCertificateComposite.setVisible(false);
		
		serverChangeCipherSpecComposite = new ServerChangeCipherSpecComposite(grp_serverComposites, SWT.NONE, this);
		serverChangeCipherSpecComposite.setLayout(new GridLayout(3, true));
		serverChangeCipherSpecComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 3));
		serverChangeCipherSpecComposite.setVisible(false);
		
		serverFinishedComposite = new ServerFinishedComposite(grp_serverComposites, SWT.NONE, this);
		serverFinishedComposite.setLayout(new GridLayout(3, true));
		serverFinishedComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 4));
		serverFinishedComposite.setVisible(false);
		
		
		//Information Panel
		Group grp_stxInfo = new Group(content, SWT.NONE);
		grp_stxInfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 4));
		grp_stxInfo.setLayout(new GridLayout());
		grp_stxInfo.setText(Messages.SslViewLblInfo);
		
		stxInformation = new StyledText(grp_stxInfo, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
		GridData gd_stxInfo = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		stxInformation.setLayoutData(gd_stxInfo);
		stxInformation.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		stxInformation.setText(Messages.SslViewStxInformation);
		stxInformation.setVisible(true);
	}
	
	/**
	 * Creates the Buttons of the GUI
	 */
	private void createButtons() {
		Group grp_buttons = new Group(mainContent, SWT.NONE);
		grp_buttons.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, false, 3, 1));
		grp_buttons.setLayout(new GridLayout(3, true));
		
		btnPreviousStep = new Button(grp_buttons, SWT.PUSH);
		btnPreviousStep.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				previousStep();
			}
		});
		btnPreviousStep.setText(Messages.SslViewBtnPreviousStep);
		btnPreviousStep.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		btnPreviousStep.setEnabled(false);
		
		btnNextStep = new Button(grp_buttons, SWT.PUSH);
		btnNextStep.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) 
			{
				nextStep();
			}
		});
		btnNextStep.setText(Messages.SslViewBtnNextStep);
		btnNextStep.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		
		Button btnReset = new Button(grp_buttons, SWT.PUSH);
		btnReset.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseUp(MouseEvent e) 
			{
				resetStep();
			}
		});
		btnReset.setText(Messages.SslViewBtnReset);
		btnReset.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
	}

	/**
	 * Sets the text of the Information-Box.
	 * All other content of the Information-Box will be deleted.
	 * @param text
	 */
	public void setStxInformationText(String text)
	{
		if(stxInformation != null) {
			stxInformation.setText(text);
		}
	}
	
	/**
	 * Returns the text of the Information-Box.
	 * @return
	 */
	public String getStxInformationText()
	{
		return stxInformation.getText();
	}
	
	/**
	 * Adds text to the Information-Box.
	 * In contrast to the function "setStxInformationText()",
	 * all other content of the Information-Box will not be deleted.
	 * @param text
	 */
	public void addTextToStxInformationText(String text)
	{
		stxInformation.setText(stxInformation.getText()+text);
	}
	
	/**
	 * This method uses the "previousStep()"-function to
	 * move from ServerHelloComposite to ClientHelloComposite
	 */
	public void backToClientHello()
	{
		arrow.moveArrowsby(18);
		arrow.nextArrow(100,75,0,75,0,0,0);
		arrow.nextArrow(0,95,100,95,0,0,0);
		previousStep();
	}
	
	/**
	 * Initializes the next Composite in the TLS-Handshake.
	 */
	public void nextStep()
	{
		if(serverHelloComposite.getVisible() == false || serverHelloComposite == null)
		{
			if(clientHelloComposite.checkParameters())
			{
				arrow.moveArrowsby(18);
				serverHelloComposite.startStep();
				serverHelloComposite.setVisible(true);
				serverHelloComposite.enableControls();
				clientHelloComposite.disableControls();
				arrow.nextArrow(0,75,100,75,0,0,0);
				btnPreviousStep.setEnabled(true);
			}
		}
		else if(serverCertificateComposite.getVisible() == false || serverCertificateComposite == null)
		{
			if(serverHelloComposite.checkParameters())
			{
				serverCertificateComposite.startStep();
				serverCertificateComposite.setVisible(true);
				serverCertificateComposite.enableControls();
				serverHelloComposite.disableControls();
			}
		}
		else if(clientCertificateComposite.getVisible() == false || clientCertificateComposite == null)
		{
			if(serverCertificateComposite.checkParameters())
			{
				clientCertificateComposite.startStep();
				clientCertificateComposite.setVisible(true);
				clientCertificateComposite.enableControls();
				serverCertificateComposite.disableControls();
				//Do not remove -Gandalf
				clientCertificateComposite.refreshInformations();
				arrow.nextArrow(100,275,0,275,0,0,0);
				if (!Message.getServerCertificateServerCertificateRequest()) 
					clientCertificateComposite.btnShow.setEnabled(false);
				//---
			}
		}
		else if(serverChangeCipherSpecComposite.getVisible() == false || serverChangeCipherSpecComposite == null)
		{
			if(clientCertificateComposite.checkParameters())
			{
				serverChangeCipherSpecComposite.startStep();
				serverChangeCipherSpecComposite.setVisible(true);
				serverChangeCipherSpecComposite.enableControls();
				serverFinishedComposite.startStep();
				serverFinishedComposite.setVisible(true);
				serverFinishedComposite.enableControls();
				clientCertificateComposite.disableControls();
				arrow.nextArrow(0,325,100,450,0,0,0);
			}
		}
		else if(clientChangeCipherSpecComposite.getVisible() == false || clientChangeCipherSpecComposite == null)
		{
			if(serverChangeCipherSpecComposite.checkParameters())
			{
				clientChangeCipherSpecComposite.startStep();
				clientChangeCipherSpecComposite.setVisible(true);
				clientChangeCipherSpecComposite.enableControls();
				clientFinishedComposite.startStep();
				clientFinishedComposite.setVisible(true);
				clientFinishedComposite.enableControls();
				serverChangeCipherSpecComposite.disableControls();
				serverFinishedComposite.disableControls();
				btnNextStep.setEnabled(false);
				arrow.nextArrow(100, 475, 0, 475, 0,0,0);
				arrow.nextArrow(0, 500, 100, 500, 0, 0, 0);
				arrow.nextArrow(100, 650, 0, 650, 0,180,0);
				arrow.nextArrow(0, 675, 100, 675, 0, 180, 0);
			}
		}
	}
	
	/**
	 * Restarts the whole Plugin.
	 */
	public void resetStep()
	{
		clientFinishedComposite.setVisible(false);
		clientFinishedComposite.resetStep();
		clientChangeCipherSpecComposite.setVisible(false);
		clientChangeCipherSpecComposite.resetStep();
		serverFinishedComposite.setVisible(false);
		serverFinishedComposite.resetStep();
		serverChangeCipherSpecComposite.setVisible(false);
		serverChangeCipherSpecComposite.resetStep();
		clientCertificateComposite.setVisible(false);
		clientCertificateComposite.resetStep();
		serverCertificateComposite.setVisible(false);
		serverCertificateComposite.resetStep();
		serverHelloComposite.setVisible(false);
		serverHelloComposite.resetStep();	
		clientHelloComposite.resetStep();
		clientHelloComposite.enableControls();
		arrow.resetArrows();
		stxInformation.setText(Messages.SslViewStxInformation);
		btnNextStep.setEnabled(true);
		btnPreviousStep.setEnabled(false);
	}
	
	/**
	 * Removes the active Composite and goes back to the previous one.
	 * All selected options in the active Composite are set back.
	 */
	public void previousStep()
	{
		if(clientChangeCipherSpecComposite.getVisible() == true)
		{
			clientFinishedComposite.setVisible(false);
			clientFinishedComposite.resetStep();
			clientChangeCipherSpecComposite.setVisible(false);
			clientChangeCipherSpecComposite.resetStep();
			serverChangeCipherSpecComposite.enableControls();
			serverFinishedComposite.enableControls();
			arrow.removeLastArrow();
			arrow.removeLastArrow();
			arrow.removeLastArrow();
			arrow.removeLastArrow();
			btnNextStep.setEnabled(true);
		}
		else if(serverChangeCipherSpecComposite.getVisible() == true)
		{
			serverFinishedComposite.setVisible(false);
			serverFinishedComposite.resetStep();
			serverChangeCipherSpecComposite.setVisible(false);
			serverChangeCipherSpecComposite.resetStep();
			clientCertificateComposite.enableControls();
			arrow.removeLastArrow();
		}
		else if(clientCertificateComposite.getVisible() == true)
		{
			clientCertificateComposite.setVisible(false);
			clientCertificateComposite.resetStep();
			serverCertificateComposite.enableControls();
			arrow.removeLastArrow();
		}
		else if(serverCertificateComposite.getVisible() == true)
		{
			serverCertificateComposite.setVisible(false);
			serverCertificateComposite.resetStep();
			serverHelloComposite.enableControls();
			serverHelloComposite.refreshInformations();
		}
		else if(serverHelloComposite.getVisible() == true)
		{
			serverHelloComposite.setVisible(false);
			serverHelloComposite.resetStep();
			clientHelloComposite.enableControls();
			clientHelloComposite.refreshInformations();
			arrow.removeLastArrow();
			btnPreviousStep.setEnabled(false);
		}
	}

	@Override
	public void setFocus() {
		// Set the focus
	}
}
