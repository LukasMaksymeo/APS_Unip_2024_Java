package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;

public class MainGame {
	
	//fundo universal:
	JFrame window; int currentWidth = 800, currentHeight = 600; 
	
	//tela de inicio -- 0
	JPanel inicio, titulo; JLabel tituloJLabel, inicioMenu; int inicioCode=0,updateInicio=0; Timer inicioTimer; 
	
	//tela de opções -- 1	
	JPanel panelOpcoes; JLabel labelOpcoes; boolean telaCheia=false; int opcoesCode=0,updateOpcoes=0,volume=7; Timer opcoesTimer;  
	
	// configurações de som no menu de opções -- 1:
	Sound sound = new Sound(); JPanel panelSom; JLabel labelSom; 
	
	//tela de texto entre batalhas -- 2
	JPanel panelTexto; JTextArea labelTexto; Timer textoTimer; int textoCode = 0; String texto; int t = 0, updateTexto, textoCarac = 0, sfx=0;	
	
	//encontros com os animais -- 3:
	JPanel panelFundo, panelMenu, panelDesc; JLabel labelMenu, labelFundo; JTextArea labelDesc; Timer descTimer, encontroTimer, ostTimer; int frames=0,updateEncontro=0,encontroCode=0,menuCode=1, t2 = 0, fase=0,updateOST=0, ostCode=0; String encontroTexto; boolean animalResgatado;
	
	//cutscene com os animais -- 4:
	JPanel panelCutscene, panelCapivara, panelCachorro, panelGato, panelTartaruga; JLabel labelCapivara, labelCachorro, labelGato, labelTartaruga; JTextArea labelCutscene; Timer cutsceneTimer; int cutsceneCode = 0, sleepTime = 3500, t3 = 0, updateCutscene = 0; String textoCutscene;
	
	//mudanças de Game State 0 = inicio --- 1 = opções --- 2 = texto --- 3 combate --- 4 cutscene --- 5 creditos:
	int gameState = 0, update = 0; Timer updateTimer; 	GraphicsDevice gDevice;

	//fontes:
	Font font, font2, font3;
	
	//controles:
	KeyHandler kHandler = new KeyHandler();
	
	public static void main(String[] args) {
		new MainGame();
	}
	
	public MainGame() {
		GraphicsEnvironment gEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		gDevice = gEnvironment.getDefaultScreenDevice();
		
		createFont();
		janela();
		telaInicio();
		telaOpcoes();
		telaEncontro();
		telaTexto();
		telaCutscene();
		update++;
		playMusic(0);
		//timer para detectar estado do jogo
		updateTimer = new Timer(10, new ActionListener() {
			
			
			
			@Override
			public void actionPerformed(ActionEvent e){
				
				if (update > 0) {
					switch(gameState) {
						case 0:
							inicio.setVisible(true);
							titulo.setVisible(true);
							panelOpcoes.setVisible(false);
							panelSom.setVisible(false);
							update=0;
							break;
						case 1:
					  		 panelOpcoes.setVisible(true);
					  		 panelSom.setVisible(true);
					  		 inicio.setVisible(false);
					  		 titulo.setVisible(false);
							update=0;
							break;
						case 2:
							playSE(4);
							
							panelTexto.setVisible(true);
							panelFundo.setVisible(false);
							panelDesc.setVisible(false);
							panelMenu.setVisible(false);
							animalResgatado=false;
							inicio.setVisible(false);
							titulo.setVisible(false);
							textoTimer.start();
							update=0;
							break;
						case 3:
							stopMusic(0);
							stopMusic(8);
							stopMusic(10);
							stopMusic(12);
							stopMusic(14);
							stopMusic(17);
							updateOST++;
							ostTimer.start();
							panelFundo.setVisible(true);
							panelDesc.setVisible(true);
							panelMenu.setVisible(true);
							panelTexto.setVisible(false);
							panelCapivara.setVisible(false);
							panelCachorro.setVisible(false);
							panelGato.setVisible(false);
							panelTartaruga.setVisible(false);
							panelCutscene.setVisible(false);
							window.getContentPane().setBackground(Color.black);
							t=0;
							update=0;
							
							break;
						case 4:
							playSE(4);
							stopMusic(17);
							playMusic(18);
							panelCapivara.setVisible(true);
							panelCutscene.setVisible(true);
							panelMenu.setVisible(false);
							panelFundo.setVisible(false);
							panelDesc.setVisible(false);
							window.getContentPane().setBackground(Color.DARK_GRAY);
							cutsceneTimer.start();
							update=0;
							break;
							
					}
				}
			}
		});
		updateTimer.start();
		inicioTimer.start();

		
	}
	
	public void setFullScreen() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		if (telaCheia==true) {
		window.remove(panelOpcoes);
		window.remove(panelSom);
		window.dispose();	
		gd.setFullScreenWindow(window);
		currentWidth = window.getWidth();
		currentHeight = window.getHeight();
		createFont();
		telaInicio();
		telaOpcoes();
		telaEncontro();
		telaTexto();
		telaCutscene();
		update++;
		}else {
			gd.setFullScreenWindow(null);
			window.remove(panelOpcoes);
			window.remove(panelSom);
			currentWidth = 800;
			currentHeight = 600;
			createFont();
			telaInicio();
			telaOpcoes();
			telaEncontro();
			telaTexto();
			update++;
		}
	}


	public void janela() {
		window = new JFrame();
		window.setSize(currentWidth,currentHeight);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.getContentPane().setBackground(Color.black);
		window.setTitle("Reciclagem Animal");
		window.setIconImage(Toolkit.getDefaultToolkit().getImage(
			    ClassLoader.getSystemResource("capivara.png")));
		window.setLocationRelativeTo(null);
		window.setLayout(null);
		window.addKeyListener(kHandler);	
		
		window.setVisible(true);
				

		
	}
	public void telaInicio() {
		
		int i_x = (int)Math.round(currentWidth*0.375);
		int i_y = (int)Math.round(currentHeight*0.4167);
		int i_w = (int)Math.round(currentWidth*0.245);
		int i_h = (int)Math.round(currentHeight*0.4133);
		
		ImageIcon inicio1 = new ImageIcon(getClass().getClassLoader().getResource("inicio1.png"));
		Image inicio1Image = inicio1.getImage();
		Image modifiedinicio1Image = inicio1Image.getScaledInstance(i_w, i_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon inicio1Resized = new ImageIcon(modifiedinicio1Image);
		
		ImageIcon inicio2 = new ImageIcon(getClass().getClassLoader().getResource("inicio2.png"));
		Image inicio2Image = inicio2.getImage();
		Image modifiedinicio2Image = inicio2Image.getScaledInstance(i_w, i_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon inicio2Resized = new ImageIcon(modifiedinicio2Image);
		
		ImageIcon inicio3 = new ImageIcon(getClass().getClassLoader().getResource("inicio3.png"));
		Image inicio3Image = inicio3.getImage();
		Image modifiedinicio3Image = inicio3Image.getScaledInstance(i_w, i_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon inicio3Resized = new ImageIcon(modifiedinicio3Image);

		inicio = new JPanel();
		inicio.setBounds(i_x, i_y, i_w, i_h);
		inicio.setOpaque(false);
		inicio.setVisible(false);
		window.getContentPane().add(inicio);
		
		inicioMenu = new JLabel();
		if (inicioCode == 0) {
		inicioMenu.setIcon(inicio1Resized);
		}else {
			inicioMenu.setIcon(inicio2Resized);
		}
		inicioMenu.setFocusable(false);
		inicioMenu.setBorder(null);
		inicio.add(inicioMenu);
		
		
		int t_x = (int)Math.round(currentWidth*0.05);
		int t_y = (int)Math.round(currentHeight*0.0333);
		int t_w = (int)Math.round(currentWidth*0.875);
		int t_h = (int)Math.round(currentHeight*0.333);
		
		ImageIcon tituloText = new ImageIcon(getClass().getClassLoader().getResource("titulo.png"));
		Image tituloTextImage = tituloText.getImage();
		Image modifiedtituloTextImage = tituloTextImage.getScaledInstance(t_w, t_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon tituloTextFS = new ImageIcon(modifiedtituloTextImage);

		
		titulo = new JPanel();
		titulo.setBounds(t_x, t_y, t_w, t_h);
		titulo.setOpaque(false);
		titulo.setVisible(false);
		window.getContentPane().add(titulo);

		tituloJLabel = new JLabel();
		tituloJLabel.setIcon(tituloTextFS);
		tituloJLabel.setFocusable(false);
		tituloJLabel.setBorder(null);
		titulo.add(tituloJLabel);
		

		inicioTimer = new Timer(10, new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e){
				   if (updateInicio > 0) {
					   switch(inicioCode) {
					   case 0:
						   inicioMenu.setIcon(inicio1Resized);
						   updateInicio = 0;
						   break;
					   case 1:
						   inicioMenu.setIcon(inicio2Resized);
						   updateInicio = 0;
						   break;
					   case 2:
						   inicioMenu.setIcon(inicio3Resized);
						   updateInicio = 0;
						   break;
					   }					   
				   }
			}
		});		
		window.setVisible(true);						
	}
	public void telaOpcoes() {
		int ps_x = (int)Math.round(currentWidth*0.45);
		int ps_y = (int)Math.round(currentHeight*0.375);
		int ps_w = (int)Math.round(currentWidth*0.1125);
		int ps_h = (int)Math.round(currentHeight*0.15);
		
		panelSom = new JPanel();
		panelSom.setBounds(ps_x, ps_y, ps_w, ps_h);
		panelSom.setOpaque(false);
		window.getContentPane().add(panelSom);
		panelSom.setVisible(false);
		
		
		labelSom = new JLabel("" + volume);
		labelSom.setBackground(Color.black);
		labelSom.setForeground(Color.white);
		labelSom.setFont(font);
		panelSom.add(labelSom);

		
		int po_x = (int)Math.round(currentWidth*0.15);
		int po_y = (int)Math.round(currentHeight*0.15);
		int po_w = (int)Math.round(currentWidth*0.6875);
		int po_h = (int)Math.round(currentHeight*0.5833);
		
		final ImageIcon opcoes1 = new ImageIcon(getClass().getClassLoader().getResource("opcoes1.png"));
		Image opcoes1Image = opcoes1.getImage();
		Image modifiedopcoes1Image = opcoes1Image.getScaledInstance(po_w, po_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon opcoes1FS = new ImageIcon(modifiedopcoes1Image);

		final ImageIcon opcoes2 = new ImageIcon(getClass().getClassLoader().getResource("opcoes2.png"));
		Image opcoes2Image = opcoes2.getImage();
		Image modifiedopcoes2Image = opcoes2Image.getScaledInstance(po_w, po_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon opcoes2FS = new ImageIcon(modifiedopcoes2Image);
		
		final ImageIcon opcoes3 = new ImageIcon(getClass().getClassLoader().getResource("opcoes3.png"));
		Image opcoes3Image = opcoes3.getImage();
		Image modifiedopcoes3Image = opcoes3Image.getScaledInstance(po_w, po_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon opcoes3FS = new ImageIcon(modifiedopcoes3Image);
		
		final ImageIcon opcoes1x = new ImageIcon(getClass().getClassLoader().getResource("opcoes1x.png"));
		Image opcoes1xImage = opcoes1x.getImage();
		Image modifiedopcoes1xImage = opcoes1xImage.getScaledInstance(1320, 630, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon opcoes1xFS = new ImageIcon(modifiedopcoes1xImage);
		
		final ImageIcon opcoes2x = new ImageIcon(getClass().getClassLoader().getResource("opcoes2x.png"));
		Image opcoes2xImage = opcoes2x.getImage();
		Image modifiedopcoes2xImage = opcoes2xImage.getScaledInstance(1320, 630, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon opcoes2xFS = new ImageIcon(modifiedopcoes2xImage);
		
		final ImageIcon opcoes3x = new ImageIcon(getClass().getClassLoader().getResource("opcoes3x.png"));
		Image opcoes3xImage = opcoes3x.getImage();
		Image modifiedopcoes3xImage = opcoes3xImage.getScaledInstance(1320, 630, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon opcoes3xFS = new ImageIcon(modifiedopcoes3xImage);
		
		panelOpcoes = new JPanel();
		panelOpcoes.setBounds(po_x, po_y, po_w, po_h);
		panelOpcoes.setOpaque(false);
		window.getContentPane().add(panelOpcoes);
		panelOpcoes.setVisible(false);
		
		labelOpcoes = new JLabel();
		labelOpcoes.setFocusable(false);
		labelOpcoes.setBorder(null);
		panelOpcoes.add(labelOpcoes);
		

		
		opcoesTimer = new Timer(10, new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e){
				   if (updateOpcoes > 0) {
					   switch(opcoesCode) {
					   case 0:
						   labelOpcoes.setIcon(opcoes1FS);
						   updateOpcoes = 0;
						   break;
					   case 1:
						   labelOpcoes.setIcon(opcoes2FS);
						   updateOpcoes = 0;
						   break;
					   case 2:
						   labelOpcoes.setIcon(opcoes3FS);
						   updateOpcoes = 0;
						   break;
					   case 3:
						   labelOpcoes.setIcon(opcoes1xFS);
						   updateOpcoes = 0;
						   break;
					   case 4:
						   labelOpcoes.setIcon(opcoes2xFS);
						   updateOpcoes = 0;
						   break;
					   case 5:
						   labelOpcoes.setIcon(opcoes3xFS);
						   updateOpcoes = 0;
						   break;
					   }					   
				   }
			}
		});	
	}
	public void telaTexto() {

		int pt_x = (int)Math.round(currentWidth*0.10);
		int pt_y = (int)Math.round(currentHeight*0.166666);
		int pt_w = (int)Math.round(currentWidth*0.8225);
		int pt_h = (int)Math.round(currentHeight*0.74666);
		
		panelTexto = new JPanel();
		panelTexto.setBounds(pt_x, pt_y, pt_w, pt_h);
		panelTexto.setBackground(Color.black);
		panelTexto.setVisible(false);

		window.getContentPane().add(panelTexto);
		
		labelTexto = new JTextArea();
		labelTexto.setBounds(pt_x, pt_y, pt_w, pt_h);
		labelTexto.setOpaque(false);
		labelTexto.setForeground(Color.cyan);
		labelTexto.setFont(font2);
		labelTexto.setLineWrap(true);
		labelTexto.setVisible(true);
		labelTexto.setWrapStyleWord(true);
		labelTexto.setEditable(false);
		panelTexto.add(labelTexto);
		
		textoTimer = new Timer(40, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				switch(encontroCode) {
				case 0:
					texto ="À beira-mar de Copacabana, sob o sol dourado do Rio de Janeiro, você, que é dedicado à preservação ambiental, caminha pela areia. Seu coração pulsando em sintonia com o ritmo das ondas. Entre os resíduos deixados pelos visitantes, você encontra uma cena que o faz agir: uma tartaruga marinha, precisando de ajuda.     APERTE ESPAÇO PARA CONTINUAR!";
					textoCarac = 316;
					break;
				case 1:				
					if (updateTexto==1) {
						labelTexto.setText("");
						updateTexto=0;
					}
					texto="Depois de ajudar a tartaruga marinha, você nota que já está na hora de ir para a escola. Chegando lá, na frente da escola, você vê algo que o surpreende e decide agir! um cachorro que esta ali na rua olhando para você esta cheio de papeis! ainda bem que você se importa com o meio ambiente e sabe como reciclar!!!      APERTE ESPAÇO PARA CONTINUAR!";
					textoCarac = 313;
					break;
				case 2:
					if (updateTexto==1) {
						labelTexto.setText("");
						updateTexto=0;
					}
					texto="Depois de ajudar a tartaruga marinha e o cachorro, você assiste suas aulas na escola. Saindo de lá e indo pra casa, em uma rua na cidade, você encontra outra cena que surpreende, uma gata que parece ter prendido sua cabeça em um pote de vidro, ainda bem que você se importa com o meio ambiente e sabe como reciclar!     APERTE ESPAÇO PARA CONTINUAR!";
					textoCarac = 316;
					break;
				case 3:
					if (updateTexto==1) {
						labelTexto.setText("");
						updateTexto=0;
					}
					texto="Depois de ajudar a tartaruga marinha, o cachorro e a gata, você ve na cidade um cartaz da grande abertura de um novo PARQUE DE DIVERSÕES! você decide ir até esse parque e se divertir um pouco, mas chegando lá você encontra... uma animal estranho te entregando um objeto..?   APERTE ESPAÇO PARA CONTINUAR";
					textoCarac = 273;
					break;
				case 4:
					if (updateTexto==1) {
						labelTexto.setText("");
						updateTexto=0;
					}
					texto="Depois de se divertir bastante no parque, você decide finalmente voltar para casa, e chegando em casa você percebe que ALGO ACONTECEU COM A SUA LIXEIRA e agora você tera que enfrentar as consequencias!!!   APERTE ESPAÇO PARA CONTINUAR";
					textoCarac = 203;
					break;
				}
				
				char character[] = texto.toCharArray();
				int arrayNumber = character.length;
				
				String addedCharacter = "";
				String blank = "";
				
				addedCharacter = blank + character[t];
				labelTexto.append(addedCharacter);
				
				if (t == textoCarac) {
					try {
						Thread.sleep(2000);
						t++;
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}else {
					t++;	
				}
				
				sfx++;
				if (sfx==9)
				{
				playSE(4);
				sfx=0;
				}
				
				if (t == arrayNumber) {
					t = 0;
					textoTimer.stop();
				}
			}
		});
}
	public void telaEncontro(){

		//valores dos panels pra FullScreen
		int pf_x = (int)Math.round(0);
		int pf_y = (int)Math.round(currentHeight*(-0.03125));
		int pf_w = (int)Math.round(currentWidth);
		int pf_h = (int)Math.round(currentHeight);
		
		int pd_x = (int)Math.round(currentWidth*0.2175);
		int pd_y = (int)Math.round(currentHeight*0.85);
		int pd_w = (int)Math.round(currentWidth*0.5825);
		int pd_h = (int)Math.round(currentHeight*0.065);
		
		int pm_x = (int)Math.round(currentWidth*0.14625);
		int pm_y = (int)Math.round(currentHeight*0.64);
		int pm_w = (int)Math.round(currentWidth*0.7075);
		int pm_h = (int)Math.round(currentHeight*0.305);
		
		//sprites da praia
		final ImageIcon praia1 = new ImageIcon(getClass().getClassLoader().getResource("praia1.png"));
		Image praia1Image = praia1.getImage();
		Image modifiedpraia1Image = praia1Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon praia1Resized = new ImageIcon(modifiedpraia1Image);
		
		final ImageIcon praia2 = new ImageIcon(getClass().getClassLoader().getResource("praia2.png"));
		Image praia2Image = praia2.getImage();
		Image modifiedpraia2Image = praia2Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon praia2Resized = new ImageIcon(modifiedpraia2Image);
		
		final ImageIcon praia3 = new ImageIcon(getClass().getClassLoader().getResource("praia3.png"));
		Image praia3Image = praia3.getImage();
		Image modifiedpraia3Image = praia3Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon praia3Resized = new ImageIcon(modifiedpraia3Image);
		
		final ImageIcon praia4 = new ImageIcon(getClass().getClassLoader().getResource("praia4.png"));
		Image praia4Image = praia4.getImage();
		Image modifiedpraia4Image = praia4Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon praia4Resized = new ImageIcon(modifiedpraia4Image);
		
		final ImageIcon praia5 = new ImageIcon(getClass().getClassLoader().getResource("praia5.png"));
		Image praia5Image = praia5.getImage();
		Image modifiedpraia5Image = praia5Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon praia5Resized = new ImageIcon(modifiedpraia5Image);
		
		final ImageIcon praia6 = new ImageIcon(getClass().getClassLoader().getResource("praia6.png"));
		Image praia6Image = praia6.getImage();
		Image modifiedpraia6Image = praia6Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon praia6Resized = new ImageIcon(modifiedpraia6Image);
		
		final ImageIcon praia7 = new ImageIcon(getClass().getClassLoader().getResource("praia7.png"));
		Image praia7Image = praia7.getImage();
		Image modifiedpraia7Image = praia7Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon praia7Resized = new ImageIcon(modifiedpraia7Image);
		
		final ImageIcon praia8 = new ImageIcon(getClass().getClassLoader().getResource("praia8.png"));
		Image praia8Image = praia8.getImage();
		Image modifiedpraia8Image = praia8Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon praia8Resized = new ImageIcon(modifiedpraia8Image);
		
		final ImageIcon praia9 = new ImageIcon(getClass().getClassLoader().getResource("praia9.png"));
		Image praia9Image = praia9.getImage();
		Image modifiedpraia9Image = praia9Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon praia9Resized = new ImageIcon(modifiedpraia9Image);
		
		final ImageIcon praia10 = new ImageIcon(getClass().getClassLoader().getResource("praia10.png"));
		Image praia10Image = praia10.getImage();
		Image modifiedpraia10Image = praia10Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon praia10Resized = new ImageIcon(modifiedpraia10Image);
		
		final ImageIcon praia11 = new ImageIcon(getClass().getClassLoader().getResource("praia11.png"));
		Image praia11Image = praia11.getImage();
		Image modifiedpraia11Image = praia11Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon praia11Resized = new ImageIcon(modifiedpraia11Image);
		
		final ImageIcon praia12 = new ImageIcon(getClass().getClassLoader().getResource("praia12.png"));
		Image praia12Image = praia12.getImage();
		Image modifiedpraia12Image = praia12Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon praia12Resized = new ImageIcon(modifiedpraia12Image);
		
		
		//sprites da escola
		final ImageIcon escola1 = new ImageIcon(getClass().getClassLoader().getResource("escola1.png"));
		Image escola1Image = escola1.getImage();
		Image modifiedescola1Image = escola1Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon escola1Resized = new ImageIcon(modifiedescola1Image);
		
		final ImageIcon escola2 = new ImageIcon(getClass().getClassLoader().getResource("escola2.png"));
		Image escola2Image = escola2.getImage();
		Image modifiedescola2Image = escola2Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon escola2Resized = new ImageIcon(modifiedescola2Image);
		
		final ImageIcon escola3 = new ImageIcon(getClass().getClassLoader().getResource("escola3.png"));
		Image escola3Image = escola3.getImage();
		Image modifiedescola3Image = escola3Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon escola3Resized = new ImageIcon(modifiedescola3Image);
		
		final ImageIcon escola4 = new ImageIcon(getClass().getClassLoader().getResource("escola4.png"));
		Image escola4Image = escola4.getImage();
		Image modifiedescola4Image = escola4Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon escola4Resized = new ImageIcon(modifiedescola4Image);
		
		final ImageIcon escola5 = new ImageIcon(getClass().getClassLoader().getResource("escola5.png"));
		Image escola5Image = escola5.getImage();
		Image modifiedescola5Image = escola5Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon escola5Resized = new ImageIcon(modifiedescola5Image);
		
		final ImageIcon escola6 = new ImageIcon(getClass().getClassLoader().getResource("escola6.png"));
		Image escola6Image = escola6.getImage();
		Image modifiedescola6Image = escola6Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon escola6Resized = new ImageIcon(modifiedescola6Image);
		
		final ImageIcon escola7 = new ImageIcon(getClass().getClassLoader().getResource("escola7.png"));
		Image escola7Image = escola7.getImage();
		Image modifiedescola7Image = escola7Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon escola7Resized = new ImageIcon(modifiedescola7Image);
		
		final ImageIcon escola8 = new ImageIcon(getClass().getClassLoader().getResource("escola8.png"));
		Image escola8Image = escola8.getImage();
		Image modifiedescola8Image = escola8Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon escola8Resized = new ImageIcon(modifiedescola8Image);
		
		final ImageIcon escola9 = new ImageIcon(getClass().getClassLoader().getResource("escola9.png"));
		Image escola9Image = escola9.getImage();
		Image modifiedescola9Image = escola9Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon escola9Resized = new ImageIcon(modifiedescola9Image);
		
		final ImageIcon escola10 = new ImageIcon(getClass().getClassLoader().getResource("escola10.png"));
		Image escola10Image = escola10.getImage();
		Image modifiedescola10Image = escola10Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon escola10Resized = new ImageIcon(modifiedescola10Image);
		
		final ImageIcon escola11 = new ImageIcon(getClass().getClassLoader().getResource("escola11.png"));
		Image escola11Image = escola11.getImage();
		Image modifiedescola11Image = escola11Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon escola11Resized = new ImageIcon(modifiedescola11Image);
		
		final ImageIcon escola12 = new ImageIcon(getClass().getClassLoader().getResource("escola12.png"));
		Image escola12Image = escola12.getImage();
		Image modifiedescola12Image = escola12Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon escola12Resized = new ImageIcon(modifiedescola12Image);
		
		//sprites da cidade
		final ImageIcon city1 = new ImageIcon(getClass().getClassLoader().getResource("city1.png"));
		Image city1Image = city1.getImage();
		Image modifiedcity1Image = city1Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon city1Resized = new ImageIcon(modifiedcity1Image);
		
		final ImageIcon city2 = new ImageIcon(getClass().getClassLoader().getResource("city2.png"));
		Image city2Image = city2.getImage();
		Image modifiedcity2Image = city2Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon city2Resized = new ImageIcon(modifiedcity2Image);
		
		final ImageIcon city3 = new ImageIcon(getClass().getClassLoader().getResource("city3.png"));
		Image city3Image = city3.getImage();
		Image modifiedcity3Image = city3Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon city3Resized = new ImageIcon(modifiedcity3Image);
		
		final ImageIcon city4 = new ImageIcon(getClass().getClassLoader().getResource("city4.png"));
		Image city4Image = city4.getImage();
		Image modifiedcity4Image = city4Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon city4Resized = new ImageIcon(modifiedcity4Image);
		
		final ImageIcon city5 = new ImageIcon(getClass().getClassLoader().getResource("city5.png"));
		Image city5Image = city5.getImage();
		Image modifiedcity5Image = city5Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon city5Resized = new ImageIcon(modifiedcity5Image);
		
		final ImageIcon city6 = new ImageIcon(getClass().getClassLoader().getResource("city6.png"));
		Image city6Image = city6.getImage();
		Image modifiedcity6Image = city6Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon city6Resized = new ImageIcon(modifiedcity6Image);
		
		final ImageIcon city7 = new ImageIcon(getClass().getClassLoader().getResource("city7.png"));
		Image city7Image = city7.getImage();
		Image modifiedcity7Image = city7Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon city7Resized = new ImageIcon(modifiedcity7Image);
		
		final ImageIcon city8 = new ImageIcon(getClass().getClassLoader().getResource("city8.png"));
		Image city8Image = city8.getImage();
		Image modifiedcity8Image = city8Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon city8Resized = new ImageIcon(modifiedcity8Image);
		
		final ImageIcon city9 = new ImageIcon(getClass().getClassLoader().getResource("city9.png"));
		Image city9Image = city9.getImage();
		Image modifiedcity9Image = city9Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon city9Resized = new ImageIcon(modifiedcity9Image);
		
		final ImageIcon city10 = new ImageIcon(getClass().getClassLoader().getResource("city10.png"));
		Image city10Image = city10.getImage();
		Image modifiedcity10Image = city10Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon city10Resized = new ImageIcon(modifiedcity10Image);
		
		final ImageIcon city11 = new ImageIcon(getClass().getClassLoader().getResource("city11.png"));
		Image city11Image = city11.getImage();
		Image modifiedcity11Image = city11Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon city11Resized = new ImageIcon(modifiedcity11Image);
		
		final ImageIcon city12 = new ImageIcon(getClass().getClassLoader().getResource("city12.png"));
		Image city12Image = city12.getImage();
		Image modifiedcity12Image = city12Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon city12Resized = new ImageIcon(modifiedcity12Image);
		
		//sprites do parque
		final ImageIcon parque1 = new ImageIcon(getClass().getClassLoader().getResource("parque1.png"));
		Image parque1Image = parque1.getImage();
		Image modifiedparque1Image = parque1Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon parque1Resized = new ImageIcon(modifiedparque1Image);
		
		final ImageIcon parque2 = new ImageIcon(getClass().getClassLoader().getResource("parque2.png"));
		Image parque2Image = parque2.getImage();
		Image modifiedparque2Image = parque2Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon parque2Resized = new ImageIcon(modifiedparque2Image);
		
		final ImageIcon parque3 = new ImageIcon(getClass().getClassLoader().getResource("parque3.png"));
		Image parque3Image = parque3.getImage();
		Image modifiedparque3Image = parque3Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon parque3Resized = new ImageIcon(modifiedparque3Image);
		
		final ImageIcon parque4 = new ImageIcon(getClass().getClassLoader().getResource("parque4.png"));
		Image parque4Image = parque4.getImage();
		Image modifiedparque4Image = parque4Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon parque4Resized = new ImageIcon(modifiedparque4Image);
		
		final ImageIcon parque5 = new ImageIcon(getClass().getClassLoader().getResource("parque5.png"));
		Image parque5Image = parque5.getImage();
		Image modifiedparque5Image = parque5Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon parque5Resized = new ImageIcon(modifiedparque5Image);
		
		final ImageIcon parque6 = new ImageIcon(getClass().getClassLoader().getResource("parque6.png"));
		Image parque6Image = parque6.getImage();
		Image modifiedparque6Image = parque6Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon parque6Resized = new ImageIcon(modifiedparque6Image);
		
		final ImageIcon parque7 = new ImageIcon(getClass().getClassLoader().getResource("parque7.png"));
		Image parque7Image = parque7.getImage();
		Image modifiedparque7Image = parque7Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon parque7Resized = new ImageIcon(modifiedparque7Image);
		
		final ImageIcon parque8 = new ImageIcon(getClass().getClassLoader().getResource("parque8.png"));
		Image parque8Image = parque8.getImage();
		Image modifiedparque8Image = parque8Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon parque8Resized = new ImageIcon(modifiedparque8Image);
		
		final ImageIcon parque9 = new ImageIcon(getClass().getClassLoader().getResource("parque9.png"));
		Image parque9Image = parque9.getImage();
		Image modifiedparque9Image = parque9Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon parque9Resized = new ImageIcon(modifiedparque9Image);
		
		final ImageIcon parque10 = new ImageIcon(getClass().getClassLoader().getResource("parque10.png"));
		Image parque10Image = parque10.getImage();
		Image modifiedparque10Image = parque10Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon parque10Resized = new ImageIcon(modifiedparque10Image);
		
		//sprites do boss
		final ImageIcon bossPapel1 = new ImageIcon(getClass().getClassLoader().getResource("bosspapel0.png"));
		Image bossPapel1Image = bossPapel1.getImage();
		Image modifiedbossPapel1Image = bossPapel1Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon bossPapel1Resized = new ImageIcon(modifiedbossPapel1Image);
		
		final ImageIcon bossPapel2 = new ImageIcon(getClass().getClassLoader().getResource("bosspapel1.png"));
		Image bossPapel2Image = bossPapel2.getImage();
		Image modifiedbossPapel2Image = bossPapel2Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon bossPapel2Resized = new ImageIcon(modifiedbossPapel2Image);
		
		final ImageIcon bossPapel3 = new ImageIcon(getClass().getClassLoader().getResource("bosspapel2.png"));
		Image bossPapel3Image = bossPapel3.getImage();
		Image modifiedbossPapel3Image = bossPapel3Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon bossPapel3Resized = new ImageIcon(modifiedbossPapel3Image);
		
		final ImageIcon bossPapel4 = new ImageIcon(getClass().getClassLoader().getResource("bosspapel3.png"));
		Image bossPapel4Image = bossPapel4.getImage();
		Image modifiedbossPapel4Image = bossPapel4Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon bossPapel4Resized = new ImageIcon(modifiedbossPapel4Image);
		
		final ImageIcon bossPapel5 = new ImageIcon(getClass().getClassLoader().getResource("bosspapel4.png"));
		Image bossPapel5Image = bossPapel5.getImage();
		Image modifiedbossPapel5Image = bossPapel5Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon bossPapel5Resized = new ImageIcon(modifiedbossPapel5Image);
		
		final ImageIcon bossPapel6 = new ImageIcon(getClass().getClassLoader().getResource("bosspapel5.png"));
		Image bossPapel6Image = bossPapel6.getImage();
		Image modifiedbossPapel6Image = bossPapel6Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon bossPapel6Resized = new ImageIcon(modifiedbossPapel6Image);
		
		
		final ImageIcon bossVidro1 = new ImageIcon(getClass().getClassLoader().getResource("bossvidro0.png"));
		Image bossVidro1Image = bossVidro1.getImage();
		Image modifiedbossVidro1Image = bossVidro1Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon bossVidro1Resized = new ImageIcon(modifiedbossVidro1Image);
		
		final ImageIcon bossVidro2 = new ImageIcon(getClass().getClassLoader().getResource("bossvidro1.png"));
		Image bossVidro2Image = bossVidro2.getImage();
		Image modifiedbossVidro2Image = bossVidro2Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon bossVidro2Resized = new ImageIcon(modifiedbossVidro2Image);
		
		final ImageIcon bossVidro3 = new ImageIcon(getClass().getClassLoader().getResource("bossvidro2.png"));
		Image bossVidro3Image = bossVidro3.getImage();
		Image modifiedbossVidro3Image = bossVidro3Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon bossVidro3Resized = new ImageIcon(modifiedbossVidro3Image);
		
		final ImageIcon bossVidro4 = new ImageIcon(getClass().getClassLoader().getResource("bossvidro3.png"));
		Image bossVidro4Image = bossVidro4.getImage();
		Image modifiedbossVidro4Image = bossVidro4Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon bossVidro4Resized = new ImageIcon(modifiedbossVidro4Image);
		
		final ImageIcon bossVidro5 = new ImageIcon(getClass().getClassLoader().getResource("bossvidro4.png"));
		Image bossVidro5Image = bossVidro5.getImage();
		Image modifiedbossVidro5Image = bossVidro5Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon bossVidro5Resized = new ImageIcon(modifiedbossVidro5Image);
		
		final ImageIcon bossVidro6 = new ImageIcon(getClass().getClassLoader().getResource("bossvidro5.png"));
		Image bossVidro6Image = bossVidro6.getImage();
		Image modifiedbossVidro6Image = bossVidro6Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon bossVidro6Resized = new ImageIcon(modifiedbossVidro6Image);
		
		
		final ImageIcon bossMetal1 = new ImageIcon(getClass().getClassLoader().getResource("bossmetal0.png"));
		Image bossMetal1Image = bossMetal1.getImage();
		Image modifiedbossMetal1Image = bossMetal1Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon bossMetal1Resized = new ImageIcon(modifiedbossMetal1Image);
		
		final ImageIcon bossMetal2 = new ImageIcon(getClass().getClassLoader().getResource("bossmetal1.png"));
		Image bossMetal2Image = bossMetal2.getImage();
		Image modifiedbossMetal2Image = bossMetal2Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon bossMetal2Resized = new ImageIcon(modifiedbossMetal2Image);
		
		final ImageIcon bossMetal3 = new ImageIcon(getClass().getClassLoader().getResource("bossmetal2.png"));
		Image bossMetal3Image = bossMetal3.getImage();
		Image modifiedbossMetal3Image = bossMetal3Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon bossMetal3Resized = new ImageIcon(modifiedbossMetal3Image);
		
		final ImageIcon bossMetal4 = new ImageIcon(getClass().getClassLoader().getResource("bossmetal3.png"));
		Image bossMetal4Image = bossMetal4.getImage();
		Image modifiedbossMetal4Image = bossMetal4Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon bossMetal4Resized = new ImageIcon(modifiedbossMetal4Image);
		
		final ImageIcon bossMetal5 = new ImageIcon(getClass().getClassLoader().getResource("bossmetal4.png"));
		Image bossMetal5Image = bossMetal5.getImage();
		Image modifiedbossMetal5Image = bossMetal5Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon bossMetal5Resized = new ImageIcon(modifiedbossMetal5Image);
		
		final ImageIcon bossMetal6 = new ImageIcon(getClass().getClassLoader().getResource("bossmetal5.png"));
		Image bossMetal6Image = bossMetal6.getImage();
		Image modifiedbossMetal6Image = bossMetal6Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon bossMetal6Resized = new ImageIcon(modifiedbossMetal6Image);
		
		final ImageIcon bossPlastico1 = new ImageIcon(getClass().getClassLoader().getResource("bossplastico0.png"));
		Image bossPlastico1Image = bossPlastico1.getImage();
		Image modifiedbossPlastico1Image = bossPlastico1Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon bossPlastico1Resized = new ImageIcon(modifiedbossPlastico1Image);
		
		final ImageIcon bossPlastico2 = new ImageIcon(getClass().getClassLoader().getResource("bossplastico1.png"));
		Image bossPlastico2Image = bossPlastico2.getImage();
		Image modifiedbossPlastico2Image = bossPlastico2Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon bossPlastico2Resized = new ImageIcon(modifiedbossPlastico2Image);
		
		final ImageIcon bossPlastico3 = new ImageIcon(getClass().getClassLoader().getResource("bossplastico2.png"));
		Image bossPlastico3Image = bossPlastico3.getImage();
		Image modifiedbossPlastico3Image = bossPlastico3Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon bossPlastico3Resized = new ImageIcon(modifiedbossPlastico3Image);
		
		final ImageIcon bossPlastico4 = new ImageIcon(getClass().getClassLoader().getResource("bossplastico3.png"));
		Image bossPlastico4Image = bossPlastico4.getImage();
		Image modifiedbossPlastico4Image = bossPlastico4Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon bossPlastico4Resized = new ImageIcon(modifiedbossPlastico4Image);
		
		final ImageIcon bossPlastico5 = new ImageIcon(getClass().getClassLoader().getResource("bossplastico4.png"));
		Image bossPlastico5Image = bossPlastico5.getImage();
		Image modifiedbossPlastico5Image = bossPlastico5Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon bossPlastico5Resized = new ImageIcon(modifiedbossPlastico5Image);
		
		final ImageIcon bossPlastico6 = new ImageIcon(getClass().getClassLoader().getResource("bossplastico5.png"));
		Image bossPlastico6Image = bossPlastico6.getImage();
		Image modifiedbossPlastico6Image = bossPlastico6Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon bossPlastico6Resized = new ImageIcon(modifiedbossPlastico6Image);
		
		
		final ImageIcon bossOrganico1 = new ImageIcon(getClass().getClassLoader().getResource("bossorganico0.png"));
		Image bossOrganico1Image = bossOrganico1.getImage();
		Image modifiedbossOrganico1Image = bossOrganico1Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon bossOrganico1Resized = new ImageIcon(modifiedbossOrganico1Image);

		final ImageIcon bossOrganico2 = new ImageIcon(getClass().getClassLoader().getResource("bossorganico1.png"));
		Image bossOrganico2Image = bossOrganico2.getImage();
		Image modifiedbossOrganico2Image = bossOrganico2Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon bossOrganico2Resized = new ImageIcon(modifiedbossOrganico2Image);
		
		final ImageIcon bossOrganico3 = new ImageIcon(getClass().getClassLoader().getResource("bossorganico2.png"));
		Image bossOrganico3Image = bossOrganico3.getImage();
		Image modifiedbossOrganico3Image = bossOrganico3Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon bossOrganico3Resized = new ImageIcon(modifiedbossOrganico3Image);
		
		final ImageIcon bossOrganico4 = new ImageIcon(getClass().getClassLoader().getResource("bossorganico3.png"));
		Image bossOrganico4Image = bossOrganico4.getImage();
		Image modifiedbossOrganico4Image = bossOrganico4Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon bossOrganico4Resized = new ImageIcon(modifiedbossOrganico4Image);
		
		final ImageIcon bossOrganico5 = new ImageIcon(getClass().getClassLoader().getResource("bossorganico4.png"));
		Image bossOrganico5Image = bossOrganico5.getImage();
		Image modifiedbossOrganico5Image = bossOrganico5Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon bossOrganico5Resized = new ImageIcon(modifiedbossOrganico5Image);

		final ImageIcon bossOrganico6 = new ImageIcon(getClass().getClassLoader().getResource("bossorganico5.png"));
		Image bossOrganico6Image = bossOrganico6.getImage();
		Image modifiedbossOrganico6Image = bossOrganico6Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon bossOrganico6Resized = new ImageIcon(modifiedbossOrganico6Image);
		
		
		final ImageIcon danoBossPapel1 = new ImageIcon(getClass().getClassLoader().getResource("danobosspapel00.png"));
		Image danoBossPapel1Image = danoBossPapel1.getImage();
		Image modifieddanoBossPapel1Image = danoBossPapel1Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossPapel1Resized = new ImageIcon(modifieddanoBossPapel1Image);
		
		final ImageIcon danoBossPapel2 = new ImageIcon(getClass().getClassLoader().getResource("danobosspapel01.png"));
		Image danoBossPapel2Image = danoBossPapel2.getImage();
		Image modifieddanoBossPapel2Image = danoBossPapel2Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossPapel2Resized = new ImageIcon(modifieddanoBossPapel2Image);
		
		final ImageIcon danoBossPapel3 = new ImageIcon(getClass().getClassLoader().getResource("danobosspapel02.png"));
		Image danoBossPapel3Image = danoBossPapel3.getImage();
		Image modifieddanoBossPapel3Image = danoBossPapel3Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossPapel3Resized = new ImageIcon(modifieddanoBossPapel3Image);

		final ImageIcon danoBossPapel4 = new ImageIcon(getClass().getClassLoader().getResource("danobosspapel03.png"));
		Image danoBossPapel4Image = danoBossPapel4.getImage();
		Image modifieddanoBossPapel4Image = danoBossPapel4Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossPapel4Resized = new ImageIcon(modifieddanoBossPapel4Image);

		final ImageIcon danoBossPapel5 = new ImageIcon(getClass().getClassLoader().getResource("danobosspapel04.png"));
		Image danoBossPapel5Image = danoBossPapel5.getImage();
		Image modifieddanoBossPapel5Image = danoBossPapel5Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossPapel5Resized = new ImageIcon(modifieddanoBossPapel5Image);

		final ImageIcon danoBossPapel6 = new ImageIcon(getClass().getClassLoader().getResource("danobosspapel05.png"));
		Image danoBossPapel6Image = danoBossPapel6.getImage();
		Image modifieddanoBossPapel6Image = danoBossPapel6Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossPapel6Resized = new ImageIcon(modifieddanoBossPapel6Image);

		final ImageIcon danoBossPapel7 = new ImageIcon(getClass().getClassLoader().getResource("danobosspapel06.png"));
		Image danoBossPapel7Image = danoBossPapel7.getImage();
		Image modifieddanoBossPapel7Image = danoBossPapel7Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossPapel7Resized = new ImageIcon(modifieddanoBossPapel7Image);

		final ImageIcon danoBossPapel8 = new ImageIcon(getClass().getClassLoader().getResource("danobosspapel07.png"));
		Image danoBossPapel8Image = danoBossPapel8.getImage();
		Image modifieddanoBossPapel8Image = danoBossPapel8Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossPapel8Resized = new ImageIcon(modifieddanoBossPapel8Image);

		final ImageIcon danoBossPapel9 = new ImageIcon(getClass().getClassLoader().getResource("danobosspapel08.png"));
		Image danoBossPapel9Image = danoBossPapel9.getImage();
		Image modifieddanoBossPapel9Image = danoBossPapel9Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossPapel9Resized = new ImageIcon(modifieddanoBossPapel9Image);

		final ImageIcon danoBossPapel10 = new ImageIcon(getClass().getClassLoader().getResource("danobosspapel09.png"));
		Image danoBossPapel10Image = danoBossPapel10.getImage();
		Image modifieddanoBossPapel10Image = danoBossPapel10Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossPapel10Resized = new ImageIcon(modifieddanoBossPapel10Image);
		
		
		final ImageIcon danoBossVidro1 = new ImageIcon(getClass().getClassLoader().getResource("danobossvidro00.png"));
		Image danoBossVidro1Image = danoBossVidro1.getImage();
		Image modifieddanoBossVidro1Image = danoBossVidro1Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossVidro1Resized = new ImageIcon(modifieddanoBossVidro1Image);

		final ImageIcon danoBossVidro2 = new ImageIcon(getClass().getClassLoader().getResource("danobossvidro01.png"));
		Image danoBossVidro2Image = danoBossVidro2.getImage();
		Image modifieddanoBossVidro2Image = danoBossVidro2Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossVidro2Resized = new ImageIcon(modifieddanoBossVidro2Image);

		final ImageIcon danoBossVidro3 = new ImageIcon(getClass().getClassLoader().getResource("danobossvidro02.png"));
		Image danoBossVidro3Image = danoBossVidro3.getImage();
		Image modifieddanoBossVidro3Image = danoBossVidro3Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossVidro3Resized = new ImageIcon(modifieddanoBossVidro3Image);

		final ImageIcon danoBossVidro4 = new ImageIcon(getClass().getClassLoader().getResource("danobossvidro03.png"));
		Image danoBossVidro4Image = danoBossVidro4.getImage();
		Image modifieddanoBossVidro4Image = danoBossVidro4Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossVidro4Resized = new ImageIcon(modifieddanoBossVidro4Image);

		final ImageIcon danoBossVidro5 = new ImageIcon(getClass().getClassLoader().getResource("danobossvidro04.png"));
		Image danoBossVidro5Image = danoBossVidro5.getImage();
		Image modifieddanoBossVidro5Image = danoBossVidro5Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossVidro5Resized = new ImageIcon(modifieddanoBossVidro5Image);

		final ImageIcon danoBossVidro6 = new ImageIcon(getClass().getClassLoader().getResource("danobossvidro05.png"));
		Image danoBossVidro6Image = danoBossVidro6.getImage();
		Image modifieddanoBossVidro6Image = danoBossVidro6Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossVidro6Resized = new ImageIcon(modifieddanoBossVidro6Image);

		final ImageIcon danoBossVidro7 = new ImageIcon(getClass().getClassLoader().getResource("danobossvidro06.png"));
		Image danoBossVidro7Image = danoBossVidro7.getImage();
		Image modifieddanoBossVidro7Image = danoBossVidro7Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossVidro7Resized = new ImageIcon(modifieddanoBossVidro7Image);

		final ImageIcon danoBossVidro8 = new ImageIcon(getClass().getClassLoader().getResource("danobossvidro07.png"));
		Image danoBossVidro8Image = danoBossVidro8.getImage();
		Image modifieddanoBossVidro8Image = danoBossVidro8Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossVidro8Resized = new ImageIcon(modifieddanoBossVidro8Image);

		final ImageIcon danoBossVidro9 = new ImageIcon(getClass().getClassLoader().getResource("danobossvidro08.png"));
		Image danoBossVidro9Image = danoBossVidro9.getImage();
		Image modifieddanoBossVidro9Image = danoBossVidro9Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossVidro9Resized = new ImageIcon(modifieddanoBossVidro9Image);

		final ImageIcon danoBossVidro10 = new ImageIcon(getClass().getClassLoader().getResource("danobossvidro09.png"));
		Image danoBossVidro10Image = danoBossVidro10.getImage();
		Image modifieddanoBossVidro10Image = danoBossVidro10Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossVidro10Resized = new ImageIcon(modifieddanoBossVidro10Image);
		
		
		final ImageIcon danoBossMetal1 = new ImageIcon(getClass().getClassLoader().getResource("danobossmetal00.png"));
		Image danoBossMetal1Image = danoBossMetal1.getImage();
		Image modifieddanoBossMetal1Image = danoBossMetal1Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossMetal1Resized = new ImageIcon(modifieddanoBossMetal1Image);

		final ImageIcon danoBossMetal2 = new ImageIcon(getClass().getClassLoader().getResource("danobossmetal01.png"));
		Image danoBossMetal2Image = danoBossMetal2.getImage();
		Image modifieddanoBossMetal2Image = danoBossMetal2Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossMetal2Resized = new ImageIcon(modifieddanoBossMetal2Image);

		final ImageIcon danoBossMetal3 = new ImageIcon(getClass().getClassLoader().getResource("danobossmetal02.png"));
		Image danoBossMetal3Image = danoBossMetal3.getImage();
		Image modifieddanoBossMetal3Image = danoBossMetal3Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossMetal3Resized = new ImageIcon(modifieddanoBossMetal3Image);

		final ImageIcon danoBossMetal4 = new ImageIcon(getClass().getClassLoader().getResource("danobossmetal03.png"));
		Image danoBossMetal4Image = danoBossMetal4.getImage();
		Image modifieddanoBossMetal4Image = danoBossMetal4Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossMetal4Resized = new ImageIcon(modifieddanoBossMetal4Image);

		final ImageIcon danoBossMetal5 = new ImageIcon(getClass().getClassLoader().getResource("danobossmetal04.png"));
		Image danoBossMetal5Image = danoBossMetal5.getImage();
		Image modifieddanoBossMetal5Image = danoBossMetal5Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossMetal5Resized = new ImageIcon(modifieddanoBossMetal5Image);

		final ImageIcon danoBossMetal6 = new ImageIcon(getClass().getClassLoader().getResource("danobossmetal05.png"));
		Image danoBossMetal6Image = danoBossMetal6.getImage();
		Image modifieddanoBossMetal6Image = danoBossMetal6Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossMetal6Resized = new ImageIcon(modifieddanoBossMetal6Image);

		final ImageIcon danoBossMetal7 = new ImageIcon(getClass().getClassLoader().getResource("danobossmetal06.png"));
		Image danoBossMetal7Image = danoBossMetal7.getImage();
		Image modifieddanoBossMetal7Image = danoBossMetal7Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossMetal7Resized = new ImageIcon(modifieddanoBossMetal7Image);

		final ImageIcon danoBossMetal8 = new ImageIcon(getClass().getClassLoader().getResource("danobossmetal07.png"));
		Image danoBossMetal8Image = danoBossMetal8.getImage();
		Image modifieddanoBossMetal8Image = danoBossMetal8Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossMetal8Resized = new ImageIcon(modifieddanoBossMetal8Image);

		final ImageIcon danoBossMetal9 = new ImageIcon(getClass().getClassLoader().getResource("danobossmetal08.png"));
		Image danoBossMetal9Image = danoBossMetal9.getImage();
		Image modifieddanoBossMetal9Image = danoBossMetal9Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossMetal9Resized = new ImageIcon(modifieddanoBossMetal9Image);

		final ImageIcon danoBossMetal10 = new ImageIcon(getClass().getClassLoader().getResource("danobossmetal09.png"));
		Image danoBossMetal10Image = danoBossMetal10.getImage();
		Image modifieddanoBossMetal10Image = danoBossMetal10Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossMetal10Resized = new ImageIcon(modifieddanoBossMetal10Image);
		
		
		final ImageIcon danoBossPlastico1 = new ImageIcon(getClass().getClassLoader().getResource("danobossplastico00.png"));
		Image danoBossPlastico1Image = danoBossPlastico1.getImage();
		Image modifieddanoBossPlastico1Image = danoBossPlastico1Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossPlastico1Resized = new ImageIcon(modifieddanoBossPlastico1Image);

		final ImageIcon danoBossPlastico2 = new ImageIcon(getClass().getClassLoader().getResource("danobossplastico01.png"));
		Image danoBossPlastico2Image = danoBossPlastico2.getImage();
		Image modifieddanoBossPlastico2Image = danoBossPlastico2Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossPlastico2Resized = new ImageIcon(modifieddanoBossPlastico2Image);

		final ImageIcon danoBossPlastico3 = new ImageIcon(getClass().getClassLoader().getResource("danobossplastico02.png"));
		Image danoBossPlastico3Image = danoBossPlastico3.getImage();
		Image modifieddanoBossPlastico3Image = danoBossPlastico3Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossPlastico3Resized = new ImageIcon(modifieddanoBossPlastico3Image);

		final ImageIcon danoBossPlastico4 = new ImageIcon(getClass().getClassLoader().getResource("danobossplastico03.png"));
		Image danoBossPlastico4Image = danoBossPlastico4.getImage();
		Image modifieddanoBossPlastico4Image = danoBossPlastico4Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossPlastico4Resized = new ImageIcon(modifieddanoBossPlastico4Image);

		final ImageIcon danoBossPlastico5 = new ImageIcon(getClass().getClassLoader().getResource("danobossplastico04.png"));
		Image danoBossPlastico5Image = danoBossPlastico5.getImage();
		Image modifieddanoBossPlastico5Image = danoBossPlastico5Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossPlastico5Resized = new ImageIcon(modifieddanoBossPlastico5Image);

		final ImageIcon danoBossPlastico6 = new ImageIcon(getClass().getClassLoader().getResource("danobossplastico05.png"));
		Image danoBossPlastico6Image = danoBossPlastico6.getImage();
		Image modifieddanoBossPlastico6Image = danoBossPlastico6Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossPlastico6Resized = new ImageIcon(modifieddanoBossPlastico6Image);

		final ImageIcon danoBossPlastico7 = new ImageIcon(getClass().getClassLoader().getResource("danobossplastico06.png"));
		Image danoBossPlastico7Image = danoBossPlastico7.getImage();
		Image modifieddanoBossPlastico7Image = danoBossPlastico7Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossPlastico7Resized = new ImageIcon(modifieddanoBossPlastico7Image);

		final ImageIcon danoBossPlastico8 = new ImageIcon(getClass().getClassLoader().getResource("danobossplastico07.png"));
		Image danoBossPlastico8Image = danoBossPlastico8.getImage();
		Image modifieddanoBossPlastico8Image = danoBossPlastico8Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossPlastico8Resized = new ImageIcon(modifieddanoBossPlastico8Image);

		final ImageIcon danoBossPlastico9 = new ImageIcon(getClass().getClassLoader().getResource("danobossplastico08.png"));
		Image danoBossPlastico9Image = danoBossPlastico9.getImage();
		Image modifieddanoBossPlastico9Image = danoBossPlastico9Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossPlastico9Resized = new ImageIcon(modifieddanoBossPlastico9Image);

		final ImageIcon danoBossPlastico10 = new ImageIcon(getClass().getClassLoader().getResource("danobossplastico09.png"));
		Image danoBossPlastico10Image = danoBossPlastico10.getImage();
		Image modifieddanoBossPlastico10Image = danoBossPlastico10Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossPlastico10Resized = new ImageIcon(modifieddanoBossPlastico10Image);
		
		
		final ImageIcon danoBossOrganico1 = new ImageIcon(getClass().getClassLoader().getResource("danobossorganico00.png"));
		Image danoBossOrganico1Image = danoBossOrganico1.getImage();
		Image modifieddanoBossOrganico1Image = danoBossOrganico1Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossOrganico1Resized = new ImageIcon(modifieddanoBossOrganico1Image);
		
		final ImageIcon danoBossOrganico2 = new ImageIcon(getClass().getClassLoader().getResource("danobossorganico01.png"));
		Image danoBossOrganico2Image = danoBossOrganico2.getImage();
		Image modifieddanoBossOrganico2Image = danoBossOrganico2Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossOrganico2Resized = new ImageIcon(modifieddanoBossOrganico2Image);
		
		final ImageIcon danoBossOrganico3 = new ImageIcon(getClass().getClassLoader().getResource("danobossorganico02.png"));
		Image danoBossOrganico3Image = danoBossOrganico3.getImage();
		Image modifieddanoBossOrganico3Image = danoBossOrganico3Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossOrganico3Resized = new ImageIcon(modifieddanoBossOrganico3Image);
		
		final ImageIcon danoBossOrganico4 = new ImageIcon(getClass().getClassLoader().getResource("danobossorganico03.png"));
		Image danoBossOrganico4Image = danoBossOrganico4.getImage();
		Image modifieddanoBossOrganico4Image = danoBossOrganico4Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossOrganico4Resized = new ImageIcon(modifieddanoBossOrganico4Image);
		
		final ImageIcon danoBossOrganico5 = new ImageIcon(getClass().getClassLoader().getResource("danobossorganico04.png"));
		Image danoBossOrganico5Image = danoBossOrganico5.getImage();
		Image modifieddanoBossOrganico5Image = danoBossOrganico5Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossOrganico5Resized = new ImageIcon(modifieddanoBossOrganico5Image);
		
		final ImageIcon danoBossOrganico6 = new ImageIcon(getClass().getClassLoader().getResource("danobossorganico05.png"));
		Image danoBossOrganico6Image = danoBossOrganico6.getImage();
		Image modifieddanoBossOrganico6Image = danoBossOrganico6Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossOrganico6Resized = new ImageIcon(modifieddanoBossOrganico6Image);
		
		final ImageIcon danoBossOrganico7 = new ImageIcon(getClass().getClassLoader().getResource("danobossorganico06.png"));
		Image danoBossOrganico7Image = danoBossOrganico7.getImage();
		Image modifieddanoBossOrganico7Image = danoBossOrganico7Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossOrganico7Resized = new ImageIcon(modifieddanoBossOrganico7Image);
		
		final ImageIcon danoBossOrganico8 = new ImageIcon(getClass().getClassLoader().getResource("danobossorganico07.png"));
		Image danoBossOrganico8Image = danoBossOrganico8.getImage();
		Image modifieddanoBossOrganico8Image = danoBossOrganico8Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossOrganico8Resized = new ImageIcon(modifieddanoBossOrganico8Image);
		
		final ImageIcon danoBossOrganico9 = new ImageIcon(getClass().getClassLoader().getResource("danobossorganico08.png"));
		Image danoBossOrganico9Image = danoBossOrganico9.getImage();
		Image modifieddanoBossOrganico9Image = danoBossOrganico9Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossOrganico9Resized = new ImageIcon(modifieddanoBossOrganico9Image);
		
		final ImageIcon danoBossOrganico10 = new ImageIcon(getClass().getClassLoader().getResource("danobossorganico09.png"));
		Image danoBossOrganico10Image = danoBossOrganico10.getImage();
		Image modifieddanoBossOrganico10Image = danoBossOrganico10Image.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon danoBossOrganico10Resized = new ImageIcon(modifieddanoBossOrganico10Image);
		
		final ImageIcon bossDerrotado = new ImageIcon(getClass().getClassLoader().getResource("bossderrotado.png"));
		Image bossDerrotadoImage = bossDerrotado.getImage();
		Image modifiedbossDerrotadoImage = bossDerrotadoImage.getScaledInstance(pf_w, pf_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon bossDerrotadoResized = new ImageIcon(modifiedbossDerrotadoImage);
		
		//sprites do menu
		final ImageIcon menuAmarelo = new ImageIcon(getClass().getClassLoader().getResource("menu_amarelo.png"));
		Image menuAmareloImage = menuAmarelo.getImage();
		Image modifiedmenuAmareloImage = menuAmareloImage.getScaledInstance(pm_w, pm_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon menuAmareloResized = new ImageIcon(modifiedmenuAmareloImage);
		
		final ImageIcon menuAzul = new ImageIcon(getClass().getClassLoader().getResource("menu_azul.png"));
		Image menuAzulImage = menuAzul.getImage();
		Image modifiedmenuAzulImage = menuAzulImage.getScaledInstance(pm_w, pm_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon menuAzulResized = new ImageIcon(modifiedmenuAzulImage);
		
		final ImageIcon menuVerde = new ImageIcon(getClass().getClassLoader().getResource("menu_verde.png"));
		Image menuVerdeImage = menuVerde.getImage();
		Image modifiedmenuVerdeImage = menuVerdeImage.getScaledInstance(pm_w, pm_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon menuVerdeResized = new ImageIcon(modifiedmenuVerdeImage);
		
		final ImageIcon menuVermelho = new ImageIcon(getClass().getClassLoader().getResource("menu_vermelho.png"));
		Image menuVermelhoImage = menuVermelho.getImage();
		Image modifiedmenuVermelhoImage = menuVermelhoImage.getScaledInstance(pm_w, pm_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon menuVermelhoResized = new ImageIcon(modifiedmenuVermelhoImage);
		
		final ImageIcon menuMarrom = new ImageIcon(getClass().getClassLoader().getResource("menu_marrom.png"));
		Image menuMarromImage = menuMarrom.getImage();
		Image modifiedmenuMarromImage = menuMarromImage.getScaledInstance(pm_w, pm_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon menuMarromResized = new ImageIcon(modifiedmenuMarromImage);
		

		//JPanels e JFrames da tela de encontro
		panelFundo = new JPanel();
		panelFundo.setBounds(pf_x, pf_y, pf_w, pf_h);
		
		panelFundo.setOpaque(false);
		panelFundo.setVisible(false);
		window.getContentPane().add(panelFundo);
		
		labelFundo = new JLabel();
		labelFundo.setBackground(Color.BLUE);
		labelFundo.setIcon(praia1Resized);
		labelFundo.setFocusable(false);
		labelFundo.setBorder(null);
		panelFundo.add(labelFundo);	
		
		panelDesc = new JPanel();
		panelDesc.setBounds(pd_x, pd_y, pd_w, pd_h);
		panelDesc.setBackground(Color.BLUE);
		panelDesc.setOpaque(false);
		panelDesc.setVisible(false);
		window.getContentPane().add(panelDesc);
		
		panelMenu = new JPanel();
		panelMenu.setBounds(pm_x, pm_y, pm_w, pm_h);
		panelMenu.setOpaque(false);
		panelMenu.setVisible(false);
		window.getContentPane().add(panelMenu);

				
		labelDesc = new JTextArea();
		labelDesc.setBounds(pd_x, pd_y, pd_w, pd_h);
		labelDesc.setOpaque(false);
		labelDesc.setForeground(Color.white);
		labelDesc.setFont(font3);
		labelDesc.setLineWrap(true);
		labelDesc.setWrapStyleWord(true);
		labelDesc.setEditable(false);
		panelDesc.add(labelDesc);
		
		labelMenu = new JLabel();
		labelMenu.setBackground(Color.BLUE);
		labelMenu.setIcon(menuAmareloResized);
		labelMenu.setFocusable(false);
		labelMenu.setBorder(null);
		panelMenu.add(labelMenu);	
		
		ostTimer = new Timer(10, new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e){
				   switch(encontroCode) {
				   	case 0:
				   		if(updateOST==1) {
				   		playMusic(7);
				   		updateOST=0;
				   		}else if(updateOST==2) {
				   		playMusic(8);
				   		updateOST=0;
				   		}break;
				   	case 1:
				   		if(updateOST==1) {
					   		playMusic(9);
					   		updateOST=0;
					   		}else if(updateOST==2) {
					   		playMusic(10);
					   		updateOST=0;
					   		}break;
				   	case 2:
				   		if(updateOST==1) {
				   		playMusic(11);
				   		updateOST=0;
				   		}else if(updateOST==2) {
				   		playMusic(12);
				   		updateOST=0;
				   		}break;
				   	case 3:
				   		if(updateOST==1) {
					   		playMusic(13);
					   		updateOST=0;
					   		}else if(updateOST==2) {
					   		playMusic(14);
					   		updateOST=0;
					   		}break;
				   	case 4:
				   		switch(updateOST) {
				   		case 1:
				   			playMusic(15);
				   			updateOST=0;
				   			break;
				   		case 2:
				   			stopMusic(15);
				   			playMusic(16);
				   			updateOST=0;
				   			break;
				   		case 3:
				   			stopMusic(16);
				   			playMusic(17);
				   			updateOST=0;
				   			break;
					   		}break;
				   		
				   		
				   }
				
			}
		});		

		//timer pros sprites
		encontroTimer = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				

				switch(encontroCode) {
					case 0:
						if(frames < 11){	
							if (animalResgatado == true) {
						           switch (frames) {
					                case 1:
					                    labelFundo.setIcon(praia7Resized);
					                    break;
					                case 2:
					                	labelFundo.setIcon(praia8Resized);
					                    break;
					                case 3:
					                	labelFundo.setIcon(praia9Resized);
					                    break;
					                case 4:
					                	labelFundo.setIcon(praia10Resized);
					                    break;
					                case 5:
					                	labelFundo.setIcon(praia11Resized);
					                    break;
					                case 6:
					                	labelFundo.setIcon(praia12Resized);
					                    break;
					                case 7: 	
					                	labelFundo.setIcon(praia11Resized);
					                    break;
					                case 8:
					                	labelFundo.setIcon(praia10Resized);
					                    break;
					                case 9:
					                	labelFundo.setIcon(praia9Resized);
					                    break;
					                case 10:
					                	labelFundo.setIcon(praia8Resized);
					                    break;
					            }
								  } else {
								           switch (frames) {
								           		case 1:
								           			labelFundo.setIcon(praia1Resized);
								           			break;
								           		case 2:
								           			labelFundo.setIcon(praia2Resized);
								           			break;
								           		case 3:
								           			labelFundo.setIcon(praia3Resized);
								           			break;
								           		case 4:
								           			labelFundo.setIcon(praia4Resized);
								           			break;
								           		case 5:
								           			labelFundo.setIcon(praia5Resized);
								           			break;
								                case 6:
								                	labelFundo.setIcon(praia6Resized);
								                    break;
								                case 7:
								                	labelFundo.setIcon(praia5Resized);
								                    break;
								                case 8:
								                	labelFundo.setIcon(praia4Resized);
								                    break;
								                case 9:
								                	labelFundo.setIcon(praia3Resized);
								                    break;
								                case 10:
								                	labelFundo.setIcon(praia2Resized);
								                	break;
								            }
								        }
								frames++;
							}
							else{
								frames = 0;
								frames++;
							}
						break;
					case 1:
						if(frames < 11){	
							if (animalResgatado == true) {
						           switch (frames) {
					                case 1:
					                    labelFundo.setIcon(escola7Resized);
					                    break;
					                case 2:
					                	labelFundo.setIcon(escola8Resized);
					                    break;
					                case 3:
					                	labelFundo.setIcon(escola9Resized);
					                    break;
					                case 4:
					                	labelFundo.setIcon(escola10Resized);
					                    break;
					                case 5:
					                	labelFundo.setIcon(escola11Resized);
					                    break;
					                case 6:
					                	labelFundo.setIcon(escola12Resized);
					                    break;
					                case 7: 	
					                	labelFundo.setIcon(escola11Resized);
					                    break;
					                case 8:
					                	labelFundo.setIcon(escola10Resized);
					                    break;
					                case 9:
					                	labelFundo.setIcon(escola9Resized);
					                    break;
					                case 10:
					                	labelFundo.setIcon(escola8Resized);
					                    break;
					            }
								  } else {
								           switch (frames) {
								           case 1:
							           			labelFundo.setIcon(escola1Resized);
							           			break;
							           		case 2:
							           			labelFundo.setIcon(escola2Resized);
							           			break;
							           		case 3:
							           			labelFundo.setIcon(escola3Resized);
							           			break;
							           		case 4:
							           			labelFundo.setIcon(escola4Resized);
							           			break;
							           		case 5:
							           			labelFundo.setIcon(escola5Resized);
							           			break;
							                case 6:
							                	labelFundo.setIcon(escola6Resized);
							                    break;
							                case 7:
							                	labelFundo.setIcon(escola5Resized);
							                    break;
							                case 8:
							                	labelFundo.setIcon(escola4Resized);
							                    break;
							                case 9:
							                	labelFundo.setIcon(escola3Resized);
							                    break;
							                case 10:
							                	labelFundo.setIcon(escola2Resized);
							                	break;
								            }
								        }
								frames++;
							}
							else{
								frames = 0;
								frames++;
							}
						break;
					case 2:
						if(frames < 11){	
							if (animalResgatado == true) {
						           switch (frames) {
					                case 1:
					                    labelFundo.setIcon(city7Resized);
					                    break;
					                case 2:
					                	labelFundo.setIcon(city8Resized);
					                    break;
					                case 3:
					                	labelFundo.setIcon(city9Resized);
					                    break;
					                case 4:
					                	labelFundo.setIcon(city10Resized);
					                    break;
					                case 5:
					                	labelFundo.setIcon(city11Resized);
					                    break;
					                case 6:
					                	labelFundo.setIcon(city12Resized);
					                    break;
					                case 7: 	
					                	labelFundo.setIcon(city11Resized);
					                    break;
					                case 8:
					                	labelFundo.setIcon(city10Resized);
					                    break;
					                case 9:
					                	labelFundo.setIcon(city9Resized);
					                    break;
					                case 10:
					                	labelFundo.setIcon(city8Resized);
					                    break;
					            }
								  } else {
								           switch (frames) {
								           case 1:
							           			labelFundo.setIcon(city1Resized);
							           			break;
							           		case 2:
							           			labelFundo.setIcon(city2Resized);
							           			break;
							           		case 3:
							           			labelFundo.setIcon(city3Resized);
							           			break;
							           		case 4:
							           			labelFundo.setIcon(city4Resized);
							           			break;
							           		case 5:
							           			labelFundo.setIcon(city5Resized);
							           			break;
							                case 6:
							                	labelFundo.setIcon(city6Resized);
							                    break;
							                case 7:
							                	labelFundo.setIcon(city5Resized);
							                    break;
							                case 8:
							                	labelFundo.setIcon(city4Resized);
							                    break;
							                case 9:
							                	labelFundo.setIcon(city3Resized);
							                    break;
							                case 10:
							                	labelFundo.setIcon(city2Resized);
							                	break;
								            }
								        }
								frames++;
							}
							else{
								frames = 0;
								frames++;
							}break;
					case 3:
						if(frames < 11){	
							if (animalResgatado == true) {
						           switch (frames) {
					                case 1:
					                    labelFundo.setIcon(parque6Resized);
					                    break;
					                case 2:
					                	labelFundo.setIcon(parque7Resized);
					                    break;
					                case 3:
					                	labelFundo.setIcon(parque8Resized);
					                    break;
					                case 4:
					                	labelFundo.setIcon(parque9Resized);
					                    break;
					                case 5:
					                	labelFundo.setIcon(parque10Resized);
					                    break;
					                case 6:
					                	labelFundo.setIcon(parque10Resized);
					                    break;
					                case 7: 	
					                	labelFundo.setIcon(parque9Resized);
					                    break;
					                case 8:
					                	labelFundo.setIcon(parque8Resized);
					                    break;
					                case 9:
					                	labelFundo.setIcon(parque7Resized);
					                    break;
					                case 10:
					                	labelFundo.setIcon(parque6Resized);
					                    break;
					            }
								  } else {
								           switch (frames) {
								           case 1:
							           			labelFundo.setIcon(parque1Resized);
							           			break;
							           		case 2:
							           			labelFundo.setIcon(parque2Resized);
							           			break;
							           		case 3:
							           			labelFundo.setIcon(parque3Resized);
							           			break;
							           		case 4:
							           			labelFundo.setIcon(parque4Resized);
							           			break;
							           		case 5:
							           			labelFundo.setIcon(parque5Resized);
							           			break;
							                case 6:
							                	labelFundo.setIcon(parque5Resized);
							                    break;
							                case 7:
							                	labelFundo.setIcon(parque4Resized);
							                    break;
							                case 8:
							                	labelFundo.setIcon(parque3Resized);
							                    break;
							                case 9:
							                	labelFundo.setIcon(parque2Resized);
							                    break;
							                case 10:
							                	labelFundo.setIcon(parque1Resized);
							                	break;
								            }
								        }
								frames++;
							}
							else{
								frames = 0;
								frames++;
							}break;
					case 4:
						if(frames < 11){	
							switch (fase) {
							case 0:
						           switch (frames) {
					                case 1:
					                    labelFundo.setIcon(bossPapel1Resized);
					                    break;
					                case 2:
					                	labelFundo.setIcon(bossPapel2Resized);
					                    break;
					                case 3:
					                	labelFundo.setIcon(bossPapel3Resized);
					                    break;
					                case 4:
					                	labelFundo.setIcon(bossPapel4Resized);
					                    break;
					                case 5:
					                	labelFundo.setIcon(bossPapel5Resized);
					                    break;
					                case 6:
					                	labelFundo.setIcon(bossPapel6Resized);
					                    break;
					                case 7: 	
					                	labelFundo.setIcon(bossPapel5Resized);
					                    break;
					                case 8:
					                	labelFundo.setIcon(bossPapel4Resized);
					                    break;
					                case 9:
					                	labelFundo.setIcon(bossPapel3Resized);
					                    break;
					                case 10:
					                	labelFundo.setIcon(bossPapel2Resized);
					                    break;
					            }break;
							case 1:
								   switch (frames) {
								   case 1:
					                    labelFundo.setIcon(danoBossPapel1Resized);
					                    break;
					                case 2:
					                	labelFundo.setIcon(danoBossPapel2Resized);
					                    break;
					                case 3:
					                	labelFundo.setIcon(danoBossPapel3Resized);
					                    break;
					                case 4:
					                	labelFundo.setIcon(danoBossPapel4Resized);
					                    break;
					                case 5:
					                	labelFundo.setIcon(danoBossPapel5Resized);
					                    break;
					                case 6:
					                	labelFundo.setIcon(danoBossPapel6Resized);
					                    break;
					                case 7: 	
					                	labelFundo.setIcon(danoBossPapel7Resized);
					                    break;
					                case 8:
					                	labelFundo.setIcon(danoBossPapel8Resized);
					                    break;
					                case 9:
					                	labelFundo.setIcon(danoBossPapel9Resized);
					                    break;
					                case 10:
					                	labelFundo.setIcon(danoBossPapel10Resized);
					                	fase=2;
					                	encontroTimer.restart();
					                    break;
					            }break;	
							case 2:
								   switch (frames) {
								   case 1:
					                    labelFundo.setIcon(bossVidro1Resized);
					                    break;
					                case 2:
					                	labelFundo.setIcon(bossVidro2Resized);
					                    break;
					                case 3:
					                	labelFundo.setIcon(bossVidro3Resized);
					                    break;
					                case 4:
					                	labelFundo.setIcon(bossVidro4Resized);
					                    break;
					                case 5:
					                	labelFundo.setIcon(bossVidro5Resized);
					                    break;
					                case 6:
					                	labelFundo.setIcon(bossVidro6Resized);
					                    break;
					                case 7: 	
					                	labelFundo.setIcon(bossVidro5Resized);
					                    break;
					                case 8:
					                	labelFundo.setIcon(bossVidro4Resized);
					                    break;
					                case 9:
					                	labelFundo.setIcon(bossVidro3Resized);
					                    break;
					                case 10:
					                	labelFundo.setIcon(bossVidro2Resized);
					                    break;
					            }break;
							case 3:
								   switch (frames) {
								   case 1:
					                    labelFundo.setIcon(danoBossVidro1Resized);
					                    break;
					                case 2:
					                	labelFundo.setIcon(danoBossVidro2Resized);
					                    break;
					                case 3:
					                	labelFundo.setIcon(danoBossVidro3Resized);
					                    break;
					                case 4:
					                	labelFundo.setIcon(danoBossVidro4Resized);
					                    break;
					                case 5:
					                	labelFundo.setIcon(danoBossVidro5Resized);
					                    break;
					                case 6:
					                	labelFundo.setIcon(danoBossVidro6Resized);
					                    break;
					                case 7: 	
					                	labelFundo.setIcon(danoBossVidro7Resized);
					                    break;
					                case 8:
					                	labelFundo.setIcon(danoBossVidro8Resized);
					                    break;
					                case 9:
					                	labelFundo.setIcon(danoBossVidro9Resized);
					                    break;
					                case 10:
					                	labelFundo.setIcon(danoBossVidro10Resized);
					                	fase=4;
					                	encontroTimer.restart();
					                    break;
					            }break;
							case 4:
								   switch (frames) {
								   case 1:
					                    labelFundo.setIcon(bossMetal1Resized);
					                    break;
					                case 2:
					                	labelFundo.setIcon(bossMetal2Resized);
					                    break;
					                case 3:
					                	labelFundo.setIcon(bossMetal3Resized);
					                    break;
					                case 4:
					                	labelFundo.setIcon(bossMetal4Resized);
					                    break;
					                case 5:
					                	labelFundo.setIcon(bossMetal5Resized);
					                    break;
					                case 6:
					                	labelFundo.setIcon(bossMetal6Resized);
					                    break;
					                case 7: 	
					                	labelFundo.setIcon(bossMetal5Resized);
					                    break;
					                case 8:
					                	labelFundo.setIcon(bossMetal4Resized);
					                    break;
					                case 9:
					                	labelFundo.setIcon(bossMetal3Resized);
					                    break;
					                case 10:
					                	labelFundo.setIcon(bossMetal2Resized);
					                    break;
					            }break;
							case 5:
								   switch (frames) {
								   case 1:
					                    labelFundo.setIcon(danoBossMetal1Resized);
					                    break;
					                case 2:
					                	labelFundo.setIcon(danoBossMetal2Resized);
					                    break;
					                case 3:
					                	labelFundo.setIcon(danoBossMetal3Resized);
					                    break;
					                case 4:
					                	labelFundo.setIcon(danoBossMetal4Resized);
					                    break;
					                case 5:
					                	labelFundo.setIcon(danoBossMetal5Resized);
					                    break;
					                case 6:
					                	labelFundo.setIcon(danoBossMetal6Resized);
					                    break;
					                case 7: 	
					                	labelFundo.setIcon(danoBossMetal7Resized);
					                    break;
					                case 8:
					                	labelFundo.setIcon(danoBossMetal8Resized);
					                    break;
					                case 9:
					                	labelFundo.setIcon(danoBossMetal9Resized);
					                    break;
					                case 10:
					                	labelFundo.setIcon(danoBossMetal10Resized);
					                	fase=6;
					                	encontroTimer.restart();
					                    break;
					            }break;
							case 6:
								   switch (frames) {
								   case 1:
					                    labelFundo.setIcon(bossPlastico1Resized);
					                    break;
					                case 2:
					                	labelFundo.setIcon(bossPlastico2Resized);
					                    break;
					                case 3:
					                	labelFundo.setIcon(bossPlastico3Resized);
					                    break;
					                case 4:
					                	labelFundo.setIcon(bossPlastico4Resized);
					                    break;
					                case 5:
					                	labelFundo.setIcon(bossPlastico5Resized);
					                    break;
					                case 6:
					                	labelFundo.setIcon(bossPlastico6Resized);
					                    break;
					                case 7: 	
					                	labelFundo.setIcon(bossPlastico5Resized);
					                    break;
					                case 8:
					                	labelFundo.setIcon(bossPlastico4Resized);
					                    break;
					                case 9:
					                	labelFundo.setIcon(bossPlastico3Resized);
					                    break;
					                case 10:
					                	labelFundo.setIcon(bossPlastico2Resized);
					                    break;
					            }break;
							case 7:
								   switch (frames) {
								   case 1:
					                    labelFundo.setIcon(danoBossPlastico1Resized);
					                    break;
					                case 2:
					                	labelFundo.setIcon(danoBossPlastico2Resized);
					                    break;
					                case 3:
					                	labelFundo.setIcon(danoBossPlastico3Resized);
					                    break;
					                case 4:
					                	labelFundo.setIcon(danoBossPlastico4Resized);
					                    break;
					                case 5:
					                	labelFundo.setIcon(danoBossPlastico5Resized);
					                    break;
					                case 6:
					                	labelFundo.setIcon(danoBossPlastico6Resized);
					                    break;
					                case 7: 	
					                	labelFundo.setIcon(danoBossPlastico7Resized);
					                    break;
					                case 8:
					                	labelFundo.setIcon(danoBossPlastico8Resized);
					                    break;
					                case 9:
					                	labelFundo.setIcon(danoBossPlastico9Resized);
					                    break;
					                case 10:
					                	labelFundo.setIcon(danoBossPlastico10Resized);
					                	fase=8;
					                	encontroTimer.restart();
					                    break;
					            }break;
							case 8:
								   switch (frames) {
								   case 1:
					                    labelFundo.setIcon(bossOrganico1Resized);
					                    break;
					                case 2:
					                	labelFundo.setIcon(bossOrganico2Resized);
					                    break;
					                case 3:
					                	labelFundo.setIcon(bossOrganico3Resized);
					                    break;
					                case 4:
					                	labelFundo.setIcon(bossOrganico4Resized);
					                    break;
					                case 5:
					                	labelFundo.setIcon(bossOrganico5Resized);
					                    break;
					                case 6:
					                	labelFundo.setIcon(bossOrganico6Resized);
					                    break;
					                case 7: 	
					                	labelFundo.setIcon(bossOrganico5Resized);
					                    break;
					                case 8:
					                	labelFundo.setIcon(bossOrganico4Resized);
					                    break;
					                case 9:
					                	labelFundo.setIcon(bossOrganico3Resized);
					                    break;
					                case 10:
					                	labelFundo.setIcon(bossOrganico2Resized);
					                    break;
					            }break;
							case 9:
								   switch (frames) {
								   case 1:
					                    labelFundo.setIcon(danoBossOrganico1Resized);
					                    break;
					                case 2:
					                	labelFundo.setIcon(danoBossOrganico2Resized);
					                    break;
					                case 3:
					                	labelFundo.setIcon(danoBossOrganico3Resized);
					                    break;
					                case 4:
					                	labelFundo.setIcon(danoBossOrganico4Resized);
					                    break;
					                case 5:
					                	labelFundo.setIcon(danoBossOrganico5Resized);
					                    break;
					                case 6:
					                	labelFundo.setIcon(danoBossOrganico6Resized);
					                    break;
					                case 7: 	
					                	labelFundo.setIcon(danoBossOrganico7Resized);
					                    break;
					                case 8:
					                	labelFundo.setIcon(danoBossOrganico8Resized);
					                    break;
					                case 9:
					                	labelFundo.setIcon(danoBossOrganico9Resized);
					                    break;
					                case 10:
					                	labelFundo.setIcon(danoBossOrganico10Resized);
					                	fase=10;
					                	encontroTimer.restart();
					                    break;
					            }break;
							case 10:
								   switch (frames) {
								   case 1:
					                    labelFundo.setIcon(bossDerrotadoResized);
					                    break;
					                case 2:
					                	labelFundo.setIcon(bossDerrotadoResized);
					                    break;
					                case 3:
					                	labelFundo.setIcon(bossDerrotadoResized);
					                    break;
					                case 4:
					                	labelFundo.setIcon(bossDerrotadoResized);
					                    break;
					                case 5:
					                	labelFundo.setIcon(bossDerrotadoResized);
					                    break;
					                case 6:
					                	labelFundo.setIcon(bossDerrotadoResized);
					                    break;
					                case 7: 	
					                	labelFundo.setIcon(bossDerrotadoResized);
					                    break;
					                case 8:
					                	labelFundo.setIcon(bossDerrotadoResized);
					                    break;
					                case 9:
					                	labelFundo.setIcon(bossDerrotadoResized);
					                    break;
					                case 10:
					                	labelFundo.setIcon(bossDerrotadoResized);
									try {
										Thread.sleep(1500);
									} catch (InterruptedException e1) {
										e1.printStackTrace();
									}
					                	System.exit(0);
					                    break;
					            }break;
								        }
								frames++;

							}
							else{
								frames = 0;
								frames++;
							}
						break;
				}
				switch (updateEncontro) {
						case 0:
							break;
						case 1:
							switch(menuCode) {
							case 1:
								labelMenu.setIcon(menuAmareloResized);
								updateEncontro=0;
								break;
							case 2:
								labelMenu.setIcon(menuAzulResized);
								updateEncontro=0;
								break;
							case 3:
								labelMenu.setIcon(menuVerdeResized);
								updateEncontro=0;
								break;
							case 4:
								labelMenu.setIcon(menuVermelhoResized);
								updateEncontro=0;
								break;
							case 5:
								labelMenu.setIcon(menuMarromResized);
								updateEncontro=0;
								break;
							}break;
						}
			}
		});
		

		//timer pro texto
		descTimer = new Timer(20, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				char character2[] = encontroTexto.toCharArray();
				int arrayNumber2 = character2.length;
				
				String addedCharacter2 = "";
				String blank = "";
				
				addedCharacter2 = blank + character2[t2];
				labelDesc.append(addedCharacter2);
				
				t2++;
				if (t2 == arrayNumber2) {
					t2 = 0;
					descTimer.stop();
				}		

			}
		});
	}
	
	public void telaCutscene() {

		//valores dos panels pra FullScreen
		int pcs_x = (int)Math.round(currentWidth *0.00625);
		int pcs_y = (int)Math.round(currentHeight*0.6666);
		int pcs_w = (int)Math.round(currentWidth*0.975);
		int pcs_h = (int)Math.round(currentHeight*0.333);
		
		int pc_x = (int)Math.round(currentWidth*0.31);
		int pc_y = (int)Math.round(currentHeight*0.25);
		int pc_w = (int)Math.round(currentWidth*0.375);
		int pc_h = (int)Math.round(currentHeight*0.5);
		
		int pg_x = (int)Math.round(currentWidth*0.6875);
		int pg_y = (int)Math.round(currentHeight*0.0333);
		int pg_w = (int)Math.round(currentWidth*0.25);
		int pg_h = (int)Math.round(currentHeight*0.333);
		
		int pch_x = (int)Math.round(currentWidth *0.025);
		int pch_y = (int)Math.round(currentHeight*0.0333);
		int pch_w = (int)Math.round(currentWidth*0.25);
		int pch_h = (int)Math.round(currentHeight*0.333);
		
		int pt_x = (int)Math.round(currentWidth*0.35);
		int pt_y = (int)Math.round(0);
		int pt_w = (int)Math.round(currentWidth*0.25);
		int pt_h = (int)Math.round(currentHeight*0.333);
				
		//sprites dos animais
		final ImageIcon tartaruga = new ImageIcon(getClass().getClassLoader().getResource("tartaruga.png"));
		Image tartarugaImage = tartaruga.getImage();
		Image modifiedtartarugaImage = tartarugaImage.getScaledInstance(pt_w, pt_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon tartarugaResized = new ImageIcon(modifiedtartarugaImage);
		
		final ImageIcon cachorro = new ImageIcon(getClass().getClassLoader().getResource("cachorro.png"));
		Image cachorroImage = cachorro.getImage();
		Image modifiedcachorroImage = cachorroImage.getScaledInstance(pch_w, pch_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon cachorroResized = new ImageIcon(modifiedcachorroImage);
		
		final ImageIcon gato = new ImageIcon(getClass().getClassLoader().getResource("gato.png"));
		Image gatoImage = gato.getImage();
		Image modifiedgatoImage = gatoImage.getScaledInstance(pg_w, pg_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon gatoResized = new ImageIcon(modifiedgatoImage);
		
		final ImageIcon capivara = new ImageIcon(getClass().getClassLoader().getResource("capivara.png"));
		Image capivaraImage = capivara.getImage();
		Image modifiedcapivaraImage = capivaraImage.getScaledInstance(pc_w, pc_h, java.awt.Image.SCALE_SMOOTH);
		final ImageIcon capivaraResized = new ImageIcon(modifiedcapivaraImage);
		

		//panel gato
		panelGato = new JPanel();
		panelGato.setBounds(pg_x, pg_y, pg_w, pg_h);
		panelGato.setBackground(Color.DARK_GRAY);
		panelGato.setVisible(false);
		window.getContentPane().add(panelGato);
				
		labelGato = new JLabel();
		labelGato.setBounds(pg_x, pg_y, pg_w, pg_h);
		labelGato.setIcon(gatoResized);
		labelGato.setForeground(Color.black);
		labelGato.setVisible(true);
		panelGato.add(labelGato);
		
		//panel dog
		panelCachorro = new JPanel();
		panelCachorro.setBounds(pch_x, pch_y, pch_w, pch_h);
		panelCachorro.setBackground(Color.DARK_GRAY);
		panelCachorro.setVisible(false);
		window.getContentPane().add(panelCachorro);
				
		labelCachorro = new JLabel();
		labelCachorro.setBounds(pch_x, pch_y, pch_w, pch_h);
		labelCachorro.setIcon(cachorroResized);
		labelCachorro.setForeground(Color.black);
		labelCachorro.setVisible(true);
		panelCachorro.add(labelCachorro);
		
		//panel tartaruga
		panelTartaruga = new JPanel();
		panelTartaruga.setBounds(pt_x, pt_y, pt_w, pt_h);
		panelTartaruga.setBackground(Color.DARK_GRAY);
		panelTartaruga.setVisible(false);
		window.getContentPane().add(panelTartaruga);
				
		labelTartaruga = new JLabel();
		labelTartaruga.setBounds(pt_x, pt_y, pt_w, pt_h);
		labelTartaruga.setIcon(tartarugaResized);
		labelTartaruga.setForeground(Color.black);
		labelTartaruga.setVisible(true);
		panelTartaruga.add(labelTartaruga);
		
		//panel vara
		panelCapivara = new JPanel();
		panelCapivara.setBounds(pc_x, pc_y, pc_w, pc_h);
		panelCapivara.setBackground(Color.DARK_GRAY);
		panelCapivara.setVisible(false);
		window.getContentPane().add(panelCapivara);
				
		labelCapivara = new JLabel();
		labelCapivara.setBounds(pc_x, pc_y, pc_w, pc_h);
		labelCapivara.setIcon(capivaraResized);
		labelCapivara.setForeground(Color.black);
		labelCapivara.setVisible(true);
		panelCapivara.add(labelCapivara);
		
		//panelCutscene
		panelCutscene = new JPanel();
		panelCutscene.setBounds(pcs_x, pcs_y, pcs_w, pcs_h);
		panelCutscene.setBackground(Color.DARK_GRAY);
		panelCutscene.setVisible(false);
		window.getContentPane().add(panelCutscene);
		
		labelCutscene = new JTextArea();
		labelCutscene.setBounds(pcs_x, pcs_y, pcs_w, pcs_h);
		labelCutscene.setOpaque(false);
		labelCutscene.setForeground(Color.white);
		labelCutscene.setFont(font2);
		labelCutscene.setLineWrap(true);
		labelCutscene.setVisible(true);
		labelCutscene.setWrapStyleWord(true);
		labelCutscene.setEditable(false);
		panelCutscene.add(labelCutscene);
		

		
		
		
		cutsceneTimer = new Timer(40, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				switch(cutsceneCode) {
				case 0:
					if (updateCutscene==1) {
						labelCutscene.setText("");
						updateCutscene=0;
					}
					textoCutscene ="     Ola, não se assuste, sou eu, a capivara que você ajudou ";
					break;
				case 1:				
					if (updateCutscene==1) {
						sleepTime=2000;										
						panelTartaruga.setVisible(true);					
						labelCutscene.setText("");
						updateCutscene=0;
					}
					textoCutscene =" Nós viemos agradecer por sua bondade e vimos que precisa de ajuda! ";
					break;
				case 2:
					if (updateCutscene==1) {
						labelCutscene.setText("");
						panelCachorro.setVisible(true);
						panelGato.setVisible(true);
						updateCutscene=0;
					}
					textoCutscene =" Por isso iremos o ajudar ensinando a você uma nova habilidade! iremos te ensinar a RECICLAR ORGANICO ";
					break;
				case 3:
					if (updateCutscene==1) {
						labelCutscene.setText("");					
						sleepTime=2000;
						updateCutscene=0;
					}
					textoCutscene ="Com essa nova habilidade você podera vencer! nós confiamos em você!!! ";
					
					break;
				}
				
				char character3[] = textoCutscene.toCharArray();
				int arrayNumber3 = character3.length;
				
				String addedCharacter3 = "";
				String blank = "";
				
				addedCharacter3 = blank + character3[t3];
				labelCutscene.append(addedCharacter3);
				sfx++;
				if (sfx==9)
				{
				playSE(4);
				sfx=0;
				}
				t3++;
				if (t3 == arrayNumber3) {
					t3 = 0;
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					cutsceneCode++;
					updateCutscene++;
					if(cutsceneCode==4) {
						cutsceneTimer.stop();
						menuCode=5;
						updateEncontro++;
						gameState=3;
						update++;
						playMusic(17);
						updateOST=3;
					}else {
					cutsceneTimer.restart();
					}
				}
			}
		});
	}
	
	
	public void createFont() {
		int font1size = (int)Math.round(currentWidth*0.08125);
		int font2size = (int)Math.round(currentWidth*0.03125);
		int font3size = (int)Math.round(currentWidth*0.02);
		font = new Font("Helvetica", Font.BOLD,font1size);
		font2 = new Font("Helvetica", Font.BOLD,font2size);
		font3 = new Font("Helvetica", Font.BOLD,font3size); //fontes

	}
		
	//ontroles
	public class KeyHandler implements KeyListener{
					
			@Override
			public void keyPressed(KeyEvent e) {

			}
			@Override
			public void keyReleased(KeyEvent e) {
				switch(e.getKeyCode()) {
				case KeyEvent.VK_W:	
					switch(gameState) {
					
					//Tela de inicio:
					case 0:
						playSE(1);
						switch(inicioCode) {
						case 0:
							inicioCode=2;
							updateInicio++;
							break;
						case 1:
							inicioCode=0;
							updateInicio++;
							break;
						case 2:
							inicioCode=1;
							updateInicio++;
							break;
					}
						break;
				  //Tela de opções:
					case 1:
						playSE(1);
						switch(opcoesCode) {
						case 0:
								opcoesCode=2;
								updateOpcoes++;
								break;							
						case 1:
								opcoesCode=0;
								updateOpcoes++;
								break;														
						case 2:
								opcoesCode=1;
								updateOpcoes++;
								break;
						case 3:
								opcoesCode=5;
								updateOpcoes++;
								break;
						case 4:
								opcoesCode=3;
								updateOpcoes++;
								break;
						case 5:
								opcoesCode=4;
								updateOpcoes++;
								break;								
						}
					case 3:		  	   			
		  	   			if (animalResgatado==true) {
			  	   			encontroCode++;
			  	   			updateTexto++;
			  	   			menuCode=1;
			  	   			gameState=2;
			  	   			update++;
			  	   			updateEncontro++;
			  	   			animalResgatado=false;
			  	   			t2=0;
			  	   			textoTimer.restart();
			  	   			encontroTimer.stop();
			  	   			descTimer.stop();
			  	   			break;
		  	   		}
				}
					break;
				case KeyEvent.VK_A:	
					switch(gameState) {
					case 1:
						switch(opcoesCode) {
						case 1:
							playSE(3);
							if(volume>=1) {
							volume-=1;
							}
							labelSom.setText("" + volume);
							sound.volumeDown();
							break;
						case 4:	
							playSE(3);
							if(volume>=1) {
							volume-=1;
							}
							labelSom.setText("" + volume);
							sound.volumeDown();
							break;
						}
					
						break;
					case 3:
						playSE(5);
					  	   switch (menuCode) {
				  	   		case 1:			  	   			
				  	   			if (animalResgatado==false) {
				  	   				menuCode = 4; 
				  	   				updateEncontro=1;	
				  	   				break;
				  	   			}else {
					  	   			encontroCode++;
					  	   			updateTexto++;
					  	   			menuCode=1;
					  	   			gameState=2;
					  	   			update++;
					  	   			updateEncontro++;
					  	   			animalResgatado=false;
					  	   			t2=0;
					  	   			textoTimer.restart();
					  	   			encontroTimer.stop();
					  	   			descTimer.stop();
					  	   			break;
				  	   		}
				  	   		case 2:
				  	   			if (animalResgatado==false) {
				  	   				menuCode = 1;
				  	   				updateEncontro=1;
				  	   				break;
				  	   			}else {
					  	   			encontroCode++;
					  	   			updateTexto++;
					  	   			menuCode=1;
					  	   			gameState=2;
					  	   			update++;
					  	   			updateEncontro++;
					  	   			animalResgatado=false;
					  	   			t2=0;
					  	   			textoTimer.restart();
					  	   			encontroTimer.stop();
					  	   			descTimer.stop();
					  	   			break;
				  	   		}

				  	   		case 3:
				  	   			if (animalResgatado==false) {
				  	   				menuCode = 2;
				  	   				updateEncontro=1;
				  	   				break;
				  	   			}else {
					  	   			encontroCode++;
					  	   			updateTexto++;
					  	   			menuCode=1;
					  	   			gameState=2;
					  	   			update++;
					  	   			updateEncontro++;
					  	   			animalResgatado=false;
					  	   			t2=0;
					  	   			textoTimer.restart();
					  	   			encontroTimer.stop();
					  	   			descTimer.stop();
					  	   			break;
				  	   		}
				  	   		case 4:
				  	   			if (animalResgatado==false) {
				  	   				menuCode = 3;
				  	   				updateEncontro=1;
				  	   				break;
				  	   			}else {
				  	   			encontroCode++;
				  	   			updateTexto++;
				  	   			menuCode=1;
				  	   			gameState=2;
				  	   			update++;
				  	   			updateEncontro++;
				  	   			animalResgatado=false;
				  	   			t2=0;
				  	   			textoTimer.restart();
				  	   			encontroTimer.stop();
				  	   			descTimer.stop();
				  	   			break;
				  	   		}
				  	   }break;
					}	  		  				  	  
				  	   break;
				  	   
				case KeyEvent.VK_S:	
					switch(gameState) {
				//tela de inicio
					case 0:
						playSE(1);
						switch(inicioCode) {
						case 0:						
							inicioCode=1;
							updateInicio++;
							break;
						case 1:
							inicioCode=2;
							updateInicio++;
							break;
						case 2:
							inicioCode=0;
							updateInicio++;
							break;			
					}
					break;
				//tela de opções:
					case 1:
						playSE(1);
						switch(opcoesCode) {
						case 0:
							opcoesCode=1;
							updateOpcoes++;
							break;							
						case 1:
							opcoesCode=2;
							updateOpcoes++;
							break;														
						case 2:
							opcoesCode=0;
							updateOpcoes++;
							break;
						case 3:
							opcoesCode=4;
							updateOpcoes++;
							break;
						case 4:
							opcoesCode=5;
							updateOpcoes++;
							break;
						case 5:
							opcoesCode=3;
							updateOpcoes++;
							break;
						}
					case 3:		  	   			
		  	   			if (animalResgatado==true) {
			  	   			encontroCode++;
			  	   			updateTexto++;
			  	   			menuCode=1;
			  	   			gameState=2;
			  	   			update++;
			  	   			updateEncontro++;
			  	   			animalResgatado=false;
			  	   			t2=0;
			  	   			textoTimer.restart();
			  	   			encontroTimer.stop();
			  	   			descTimer.stop();
			  	   			break;
		  	   		}
				}
					break;
				case KeyEvent.VK_D:
					switch(gameState) {
					case 1:
						switch(opcoesCode) {
						case 1:
							playSE(3);
							if(volume<=9) {
							volume+=1;
							}
							labelSom.setText("" + volume);
							sound.volumeUp();
							break;
						case 4:	
							playSE(3);
							if(volume<=9) {
							volume+=1;
							}
							labelSom.setText("" + volume);
							sound.volumeUp();
							break;
						}
					case 3:
						playSE(5);
						  	   switch (menuCode) {
					  	   		case 1:			  	   			
					  	   			if (animalResgatado==false) {
					  	   				menuCode = 2; 
					  	   				updateEncontro=1;	
					  	   				break;
					  	   			}else {
						  	   			encontroCode++;
						  	   			updateTexto++;
						  	   			menuCode=1;
						  	   			gameState=2;
						  	   			update++;
						  	   			updateEncontro++;
						  	   			animalResgatado=false;
						  	   			t2=0;
						  	   			textoTimer.restart();
						  	   			encontroTimer.stop();
						  	   			descTimer.stop();
						  	   			break;
					  	   		}
					  	   		case 2:			  	   			
					  	   			if (animalResgatado==false) {
					  	   				menuCode = 3; 
					  	   				updateEncontro=1;	
					  	   				break;
					  	   			}else {
						  	   			encontroCode++;
						  	   			updateTexto++;
						  	   			menuCode=1;
						  	   			gameState=2;
						  	   			update++;
						  	   			updateEncontro++;
						  	   			animalResgatado=false;
						  	   			t2=0;
						  	   			textoTimer.restart();
						  	   			encontroTimer.stop();
						  	   			descTimer.stop();
						  	   			break;
					  	   		}
					  	   		case 3:			  	   			
					  	   			if (animalResgatado==false) {
					  	   				menuCode = 4; 
					  	   				updateEncontro=1;	
					  	   				break;
					  	   			}else {
						  	   			encontroCode++;
						  	   			updateTexto++;
						  	   			menuCode=1;
						  	   			gameState=2;
						  	   			update++;
						  	   			updateEncontro++;
						  	   			animalResgatado=false;
						  	   			t2=0;
						  	   			textoTimer.restart();
						  	   			encontroTimer.stop();
						  	   			descTimer.stop();
						  	   			break;
					  	   		}
					  	   		case 4:			  	   			
					  	   			if (animalResgatado==false) {
					  	   				menuCode = 1; 
					  	   				updateEncontro=1;	
					  	   				break;
					  	   			}else {
						  	   			encontroCode++;
						  	   			updateTexto++;
						  	   			menuCode=1;
						  	   			gameState=2;
						  	   			update++;
						  	   			updateEncontro++;
						  	   			animalResgatado=false;
						  	   			t2=0;
						  	   			textoTimer.restart();
						  	   			encontroTimer.stop();
						  	   			descTimer.stop();
						  	   			break;
					  	   		}
					  	   }break;
					}	  		  				  	  
				  	   break;


				case KeyEvent.VK_SPACE:
					switch(gameState) {
					case 0: //tela de inicio
						playSE(2);
						switch(inicioCode) {
						case 0:
							gameState = 2;
							inicioTimer.stop();
							update++;
							break;
						case 1:
							gameState = 1;
							opcoesTimer.start();
							inicioTimer.stop();
							updateOpcoes++;
							update++;
							break;
						case 2:
							System.exit(0);
							break;						
					}break;
					case 1: // tela de opções
						switch(opcoesCode) {
						case 0:
							playSE(2);
							telaCheia=true;
							setFullScreen();
							opcoesCode=3;
							updateOpcoes++;
							break;
						case 1:
							break;
						case 2:
							playSE(2);
							gameState=0;
							inicioTimer.start();
							opcoesTimer.stop();
							update++;
							break;
						case 3:
							playSE(2);
							telaCheia=false;
							setFullScreen();
							opcoesCode=0;
							updateOpcoes++;
							break;
						case 4:
							break;
						case 5:
							playSE(2);
							gameState=0;
							inicioTimer.start();
							opcoesTimer.stop();
							update++;
							break;
						}break;
					case 2: //tela de texto
						switch (encontroCode) {
						case 0:
							stopMusic(4);
							encontroTexto = "Você vê uma TARTARUGA com uma SACOLA PLASTICA!";
							labelDesc.setText("");
							gameState=3;
							descTimer.restart();
							encontroTimer.start();
							break;
						case 1:
							stopMusic(4);
							encontroTexto = "Você achou um CACHORRO com PAPÉIS grudados nele!";
							labelDesc.setText("");
							gameState=3;
							descTimer.restart();
							encontroTimer.start();
							break;
						case 2:
							stopMusic(4);
							encontroTexto = "Você encontrou uma GATA presa em um POTE DE VIDRO!";
							labelDesc.setText("");
							gameState=3;
							descTimer.restart();
							encontroTimer.start();
							break;
						case 3:
							stopMusic(4);
							encontroTexto = "Você encontrou uma CAPIVARA com uma LATINHA!";
							labelDesc.setText("");
							gameState=3;
							descTimer.restart();
							encontroTimer.start();
							break;
						case 4:
							stopMusic(4);
							encontroTexto = "INIMIGO DA RECICLAGEM! ele esta segurando um panfleto!";
							labelDesc.setText("");
							gameState=3;
							descTimer.restart();
							encontroTimer.start();
							break;
						}
						
						update++;
						textoTimer.stop();
						break;
					case 3: //tela de encontro
						switch (menuCode) {
						case 1:					
							switch(encontroCode) {
							case 0:
								playSE(3);
								break;
							case 1:
								playSE(3);
								break;
							case 2:
								playSE(3);
								break;
							case 3 :
								playSE(6);
								if(animalResgatado==false) {
									animalResgatado=true;
									stopMusic(13);
									updateOST=2;
									labelDesc.setText("");
									t2=0;
									encontroTexto=" Parabéns! você ajudou a CAPIVARA, aperte qualquer tecla!";
									descTimer.restart();
									break;
								}else{
									encontroCode++;
					  	   			updateTexto++;
					  	   			menuCode=1;
					  	   			gameState=2;
					  	   			update++;
					  	   			updateEncontro++;
					  	   			animalResgatado=false;
					  	   			t2=0;
					  	   			textoTimer.restart();
					  	   			encontroTimer.stop();
					  	   			descTimer.stop();
					  	   			break;
								}
							case 4:
								switch(fase){
								case 0:
									playSE(3);
									break;
								case 2:
									playSE(3);
									break;
								case 4:
									playSE(6);
									fase++;
									frames=0;
									labelDesc.setText("");
									t2=0;
									encontroTexto="          Boa! agora ele ta com uma sacola!";
									descTimer.restart();
									break;
								case 6:
									playSE(3);
									break;
								case 8:
									playSE(3);
									labelDesc.setText("");
									gameState=4;
									update++;
									break;
								}break;
						}
							break;
						case 2:
							switch(encontroCode) {
							case 0:
								playSE(3);
								break;
							case 1:
								if(animalResgatado==false) {
									playSE(6);
									animalResgatado=true;
									stopMusic(9);
									updateOST=2;
									labelDesc.setText("");
									t2=0;
									encontroTexto="Boa! você ajudou o CACHORRO, aperte qualquer tecla!";
									descTimer.restart();
									break;
								}else{
									encontroCode++;
					  	   			updateTexto++;
					  	   			menuCode=1;
					  	   			gameState=2;
					  	   			update++;
					  	   			updateEncontro++;
					  	   			animalResgatado=false;
					  	   			t2=0;
					  	   			textoTimer.restart();
					  	   			encontroTimer.stop();
					  	   			descTimer.stop();
					  	   			break;
								}
							case 2:
								playSE(3);
								break;
							case 3:
								playSE(3);
								break;
							case 4:
								switch(fase){
								case 0:
									playSE(6);
									updateOST=2;
									fase++;
									frames=0;
									labelDesc.setText("");
									t2=0;
									encontroTexto="         Boa! agora ele ta com uma garrafa de vidro!";
									descTimer.restart();
									break;
								case 2:
									playSE(3);
									break;
								case 4:
									playSE(3);
									break;
								case 6:
									playSE(3);
									break;
								case 8:
									playSE(3);
									labelDesc.setText("");
									gameState=4;
									update++;
									break;
								}break;
						}
							break;
						case 3:
							switch(encontroCode) {
							case 0:
								playSE(3);
								break;
							case 1:
								playSE(3);
								break;
							case 2:
								if(animalResgatado==false) {
									playSE(6);
									animalResgatado=true;
									stopMusic(11);
									updateOST=2;
									labelDesc.setText("");
									t2=0;
									encontroTexto="    Parabéns! você ajudou a GATA, aperte qualquer tecla!";
									descTimer.restart();
									break;
								}else{
									encontroCode++;
					  	   			updateTexto++;
					  	   			menuCode=1;
					  	   			gameState=2;
					  	   			update++;
					  	   			updateEncontro++;
					  	   			animalResgatado=false;
					  	   			t2=0;
					  	   			textoTimer.restart();
					  	   			encontroTimer.stop();
					  	   			descTimer.stop();
					  	   			break;
								}
							case 3:
								playSE(3);
								break;
							case 4:
									switch(fase) {
									case 0:
										playSE(3);
										break;
									case 2:
										playSE(6);
										fase++;
										frames=0;
										labelDesc.setText("");
										t2=0;
										encontroTexto="                    Boa! agora ele tem uma lata!";
										descTimer.restart();
										break;
									case 4:
										playSE(3);
										break;
									case 6:
										playSE(3);
										break;
									case 8:
										playSE(6);
										labelDesc.setText("");
										gameState=4;
										update++;
										break;
									}break;
							}										
							break;
						case 4:
							switch(encontroCode) {
							case 0:
								if(animalResgatado==false) {
									playSE(6);
									animalResgatado=true;
									stopMusic(7);
									updateOST=2;
									labelDesc.setText("");
									t2=0;
									encontroTexto="Parabéns! você ajudou a TARTARUGA, aperte qualquer tecla!";
									descTimer.restart();
									break;
								}else{
									encontroCode++;
					  	   			updateTexto++;
					  	   			menuCode=1;
					  	   			gameState=2;
					  	   			update++;
					  	   			updateEncontro++;
					  	   			animalResgatado=false;
					  	   			t2=0;
					  	   			textoTimer.restart();
					  	   			encontroTimer.stop();
					  	   			descTimer.stop();
					  	   			break;
								}
							case 1:
								playSE(3);
								break;
							case 2:
								playSE(3);
								break;
							case 3:
								playSE(3);
								break;
								
								case 4:
									switch(fase) {
									case 0:
										playSE(3);
										break;
									case 2:
										playSE(3);
										break;
									case 4:
										playSE(3);
										break;
									case 6:
										playSE(6);
										updateOST=3;
										fase++;
										frames=0;
										labelDesc.setText("");
										t2=0;
										descTimer.restart();
										encontroTexto="           ESSA NÃO! agora ele tem restos de comida?!";
										break;
									case 8:
										playSE(3);
										labelDesc.setText("");
										gameState=4;
										update++;
									}break;								
							}
							break;
						case 5:
							if (animalResgatado==false) {
							playSE(6);
							playSE(19);
							stopMusic(17);
							stopMusic(17); 
							fase++;
							frames=0;
							animalResgatado=true;
							labelDesc.setText("");
							t2=0;
							descTimer.restart();
							encontroTexto="";
							}
						}
						break;
				}break;
				//controles na setinha e no enter ao inves de wasd e espaço
				case KeyEvent.VK_UP:	
					switch(gameState) {
					
					//Tela de inicio:
					case 0:
						playSE(1);
						switch(inicioCode) {
						case 0:
							inicioCode=2;
							updateInicio++;
							break;
						case 1:
							inicioCode=0;
							updateInicio++;
							break;
						case 2:
							inicioCode=1;
							updateInicio++;
							break;
					}
						break;
				  //Tela de opções:
					case 1:
						playSE(1);
						switch(opcoesCode) {
						case 0:
								opcoesCode=2;
								updateOpcoes++;
								break;							
						case 1:
								opcoesCode=0;
								updateOpcoes++;
								break;														
						case 2:
								opcoesCode=1;
								updateOpcoes++;
								break;
						case 3:
								opcoesCode=5;
								updateOpcoes++;
								break;
						case 4:
								opcoesCode=3;
								updateOpcoes++;
								break;
						case 5:
								opcoesCode=4;
								updateOpcoes++;
								break;								
						}
					case 3:		  	   			
		  	   			if (animalResgatado==true) {
			  	   			encontroCode++;
			  	   			updateTexto++;
			  	   			menuCode=1;
			  	   			gameState=2;
			  	   			update++;
			  	   			updateEncontro++;
			  	   			animalResgatado=false;
			  	   			t2=0;
			  	   			textoTimer.restart();
			  	   			encontroTimer.stop();
			  	   			descTimer.stop();
			  	   			break;
		  	   		}
				}
					break;
				case KeyEvent.VK_LEFT:	
					switch(gameState) {
					case 1:
						switch(opcoesCode) {
						case 1:
							playSE(3);
							if(volume>=1) {
							volume-=1;
							}
							labelSom.setText("" + volume);
							sound.volumeDown();
							break;
						case 4:	
							playSE(3);
							if(volume>=1) {
							volume-=1;
							}
							labelSom.setText("" + volume);
							sound.volumeDown();
							break;
						}
					
						break;
					case 3:
						playSE(5);
					  	   switch (menuCode) {
				  	   		case 1:			  	   			
				  	   			if (animalResgatado==false) {
				  	   				menuCode = 4; 
				  	   				updateEncontro=1;	
				  	   				break;
				  	   			}else {
					  	   			encontroCode++;
					  	   			updateTexto++;
					  	   			menuCode=1;
					  	   			gameState=2;
					  	   			update++;
					  	   			updateEncontro++;
					  	   			animalResgatado=false;
					  	   			t2=0;
					  	   			textoTimer.restart();
					  	   			encontroTimer.stop();
					  	   			descTimer.stop();
					  	   			break;
				  	   		}
				  	   		case 2:
				  	   			if (animalResgatado==false) {
				  	   				menuCode = 1;
				  	   				updateEncontro=1;
				  	   				break;
				  	   			}else {
					  	   			encontroCode++;
					  	   			updateTexto++;
					  	   			menuCode=1;
					  	   			gameState=2;
					  	   			update++;
					  	   			updateEncontro++;
					  	   			animalResgatado=false;
					  	   			t2=0;
					  	   			textoTimer.restart();
					  	   			encontroTimer.stop();
					  	   			descTimer.stop();
					  	   			break;
				  	   		}

				  	   		case 3:
				  	   			if (animalResgatado==false) {
				  	   				menuCode = 2;
				  	   				updateEncontro=1;
				  	   				break;
				  	   			}else {
					  	   			encontroCode++;
					  	   			updateTexto++;
					  	   			menuCode=1;
					  	   			gameState=2;
					  	   			update++;
					  	   			updateEncontro++;
					  	   			animalResgatado=false;
					  	   			t2=0;
					  	   			textoTimer.restart();
					  	   			encontroTimer.stop();
					  	   			descTimer.stop();
					  	   			break;
				  	   		}
				  	   		case 4:
				  	   			if (animalResgatado==false) {
				  	   				menuCode = 3;
				  	   				updateEncontro=1;
				  	   				break;
				  	   			}else {
				  	   			encontroCode++;
				  	   			updateTexto++;
				  	   			menuCode=1;
				  	   			gameState=2;
				  	   			update++;
				  	   			updateEncontro++;
				  	   			animalResgatado=false;
				  	   			t2=0;
				  	   			textoTimer.restart();
				  	   			encontroTimer.stop();
				  	   			descTimer.stop();
				  	   			break;
				  	   		}
				  	   }break;
					}	  		  				  	  
				  	   break;
				  	   
				case KeyEvent.VK_DOWN:	
					switch(gameState) {
				//tela de inicio
					case 0:
						playSE(1);
						switch(inicioCode) {
						case 0:						
							inicioCode=1;
							updateInicio++;
							break;
						case 1:
							inicioCode=2;
							updateInicio++;
							break;
						case 2:
							inicioCode=0;
							updateInicio++;
							break;			
					}
					break;
				//tela de opções:
					case 1:
						playSE(1);
						switch(opcoesCode) {
						case 0:
							opcoesCode=1;
							updateOpcoes++;
							break;							
						case 1:
							opcoesCode=2;
							updateOpcoes++;
							break;														
						case 2:
							opcoesCode=0;
							updateOpcoes++;
							break;
						case 3:
							opcoesCode=4;
							updateOpcoes++;
							break;
						case 4:
							opcoesCode=5;
							updateOpcoes++;
							break;
						case 5:
							opcoesCode=3;
							updateOpcoes++;
							break;
						}
					case 3:		  	   			
		  	   			if (animalResgatado==true) {
			  	   			encontroCode++;
			  	   			updateTexto++;
			  	   			menuCode=1;
			  	   			gameState=2;
			  	   			update++;
			  	   			updateEncontro++;
			  	   			animalResgatado=false;
			  	   			t2=0;
			  	   			textoTimer.restart();
			  	   			encontroTimer.stop();
			  	   			descTimer.stop();
			  	   			break;
		  	   		}
				}
					break;
				case KeyEvent.VK_RIGHT:
					switch(gameState) {
					case 1:
						switch(opcoesCode) {
						case 1:
							playSE(3);
							if(volume<=9) {
							volume+=1;
							}
							labelSom.setText("" + volume);
							sound.volumeUp();
							break;
						case 4:	
							playSE(3);
							if(volume<=9) {
							volume+=1;
							}
							labelSom.setText("" + volume);
							sound.volumeUp();
							break;
						}
					case 3:
						playSE(5);
						  	   switch (menuCode) {
					  	   		case 1:			  	   			
					  	   			if (animalResgatado==false) {
					  	   				menuCode = 2; 
					  	   				updateEncontro=1;	
					  	   				break;
					  	   			}else {
						  	   			encontroCode++;
						  	   			updateTexto++;
						  	   			menuCode=1;
						  	   			gameState=2;
						  	   			update++;
						  	   			updateEncontro++;
						  	   			animalResgatado=false;
						  	   			t2=0;
						  	   			textoTimer.restart();
						  	   			encontroTimer.stop();
						  	   			descTimer.stop();
						  	   			break;
					  	   		}
					  	   		case 2:			  	   			
					  	   			if (animalResgatado==false) {
					  	   				menuCode = 3; 
					  	   				updateEncontro=1;	
					  	   				break;
					  	   			}else {
						  	   			encontroCode++;
						  	   			updateTexto++;
						  	   			menuCode=1;
						  	   			gameState=2;
						  	   			update++;
						  	   			updateEncontro++;
						  	   			animalResgatado=false;
						  	   			t2=0;
						  	   			textoTimer.restart();
						  	   			encontroTimer.stop();
						  	   			descTimer.stop();
						  	   			break;
					  	   		}
					  	   		case 3:			  	   			
					  	   			if (animalResgatado==false) {
					  	   				menuCode = 4; 
					  	   				updateEncontro=1;	
					  	   				break;
					  	   			}else {
						  	   			encontroCode++;
						  	   			updateTexto++;
						  	   			menuCode=1;
						  	   			gameState=2;
						  	   			update++;
						  	   			updateEncontro++;
						  	   			animalResgatado=false;
						  	   			t2=0;
						  	   			textoTimer.restart();
						  	   			encontroTimer.stop();
						  	   			descTimer.stop();
						  	   			break;
					  	   		}
					  	   		case 4:			  	   			
					  	   			if (animalResgatado==false) {
					  	   				menuCode = 1; 
					  	   				updateEncontro=1;	
					  	   				break;
					  	   			}else {
						  	   			encontroCode++;
						  	   			updateTexto++;
						  	   			menuCode=1;
						  	   			gameState=2;
						  	   			update++;
						  	   			updateEncontro++;
						  	   			animalResgatado=false;
						  	   			t2=0;
						  	   			textoTimer.restart();
						  	   			encontroTimer.stop();
						  	   			descTimer.stop();
						  	   			break;
					  	   		}
					  	   }break;
					}	  		  				  	  
				  	   break;


				case KeyEvent.VK_ENTER:
					switch(gameState) {
					case 0: //tela de inicio
						playSE(2);
						switch(inicioCode) {
						case 0:
							gameState = 2;
							inicioTimer.stop();
							update++;
							break;
						case 1:
							gameState = 1;
							opcoesTimer.start();
							inicioTimer.stop();
							updateOpcoes++;
							update++;
							break;
						case 2:
							System.exit(0);
							break;						
					}break;
					case 1: // tela de opções
						switch(opcoesCode) {
						case 0:
							playSE(2);
							telaCheia=true;
							setFullScreen();
							opcoesCode=3;
							updateOpcoes++;
							break;
						case 1:
							break;
						case 2:
							playSE(2);
							gameState=0;
							inicioTimer.start();
							opcoesTimer.stop();
							update++;
							break;
						case 3:
							playSE(2);
							telaCheia=false;
							setFullScreen();
							opcoesCode=0;
							updateOpcoes++;
							break;
						case 4:
							break;
						case 5:
							playSE(2);
							gameState=0;
							inicioTimer.start();
							opcoesTimer.stop();
							update++;
							break;
						}break;
					case 2: //tela de texto
						switch (encontroCode) {
						case 0:
							stopMusic(4);
							encontroTexto = "Você vê uma TARTARUGA com uma SACOLA PLASTICA!";
							labelDesc.setText("");
							gameState=3;
							descTimer.restart();
							encontroTimer.start();
							break;
						case 1:
							stopMusic(4);
							encontroTexto = "Você achou um CACHORRO com PAPÉIS grudados nele!";
							labelDesc.setText("");
							gameState=3;
							descTimer.restart();
							encontroTimer.start();
							break;
						case 2:
							stopMusic(4);
							encontroTexto = "Você encontrou uma GATA presa em um POTE DE VIDRO!";
							labelDesc.setText("");
							gameState=3;
							descTimer.restart();
							encontroTimer.start();
							break;
						case 3:
							stopMusic(4);
							encontroTexto = "Você encontrou uma CAPIVARA com uma LATINHA!";
							labelDesc.setText("");
							gameState=3;
							descTimer.restart();
							encontroTimer.start();
							break;
						case 4:
							stopMusic(4);
							encontroTexto = "INIMIGO DA RECICLAGEM! ele esta segurando um panfleto!";
							labelDesc.setText("");
							gameState=3;
							descTimer.restart();
							encontroTimer.start();
							break;
						}
						
						update++;
						textoTimer.stop();
						break;
					case 3: //tela de encontro
						switch (menuCode) {
						case 1:					
							switch(encontroCode) {
							case 0:
								playSE(3);
								break;
							case 1:
								playSE(3);
								break;
							case 2:
								playSE(3);
								break;
							case 3 :
								playSE(6);
								if(animalResgatado==false) {
									animalResgatado=true;
									stopMusic(13);
									updateOST=2;
									labelDesc.setText("");
									t2=0;
									encontroTexto=" Parabéns! você ajudou a CAPIVARA, aperte qualquer tecla!";
									descTimer.restart();
									break;
								}else{
									encontroCode++;
					  	   			updateTexto++;
					  	   			menuCode=1;
					  	   			gameState=2;
					  	   			update++;
					  	   			updateEncontro++;
					  	   			animalResgatado=false;
					  	   			t2=0;
					  	   			textoTimer.restart();
					  	   			encontroTimer.stop();
					  	   			descTimer.stop();
					  	   			break;
								}
							case 4:
								switch(fase){
								case 0:
									playSE(3);
									break;
								case 2:
									playSE(3);
									break;
								case 4:
									playSE(6);
									fase++;
									frames=0;
									labelDesc.setText("");
									t2=0;
									encontroTexto="          Boa! agora ele ta com uma sacola!";
									descTimer.restart();
									break;
								case 6:
									playSE(3);
									break;
								case 8:
									playSE(3);
									labelDesc.setText("");
									gameState=4;
									update++;
									break;
								}break;
						}
							break;
						case 2:
							switch(encontroCode) {
							case 0:
								playSE(3);
								break;
							case 1:
								if(animalResgatado==false) {
									playSE(6);
									animalResgatado=true;
									stopMusic(9);
									updateOST=2;
									labelDesc.setText("");
									t2=0;
									encontroTexto="Boa! você ajudou o CACHORRO, aperte qualquer tecla!";
									descTimer.restart();
									break;
								}else{
									encontroCode++;
					  	   			updateTexto++;
					  	   			menuCode=1;
					  	   			gameState=2;
					  	   			update++;
					  	   			updateEncontro++;
					  	   			animalResgatado=false;
					  	   			t2=0;
					  	   			textoTimer.restart();
					  	   			encontroTimer.stop();
					  	   			descTimer.stop();
					  	   			break;
								}
							case 2:
								playSE(3);
								break;
							case 3:
								playSE(3);
								break;
							case 4:
								switch(fase){
								case 0:
									playSE(6);
									updateOST=2;
									fase++;
									frames=0;
									labelDesc.setText("");
									t2=0;
									encontroTexto="         Boa! agora ele ta com uma garrafa de vidro!";
									descTimer.restart();
									break;
								case 2:
									playSE(3);
									break;
								case 4:
									playSE(3);
									break;
								case 6:
									playSE(3);
									break;
								case 8:
									playSE(3);
									labelDesc.setText("");
									gameState=4;
									update++;
									break;
								}break;
						}
							break;
						case 3:
							switch(encontroCode) {
							case 0:
								playSE(3);
								break;
							case 1:
								playSE(3);
								break;
							case 2:
								if(animalResgatado==false) {
									playSE(6);
									animalResgatado=true;
									stopMusic(11);
									updateOST=2;
									labelDesc.setText("");
									t2=0;
									encontroTexto="    Parabéns! você ajudou a GATA, aperte qualquer tecla!";
									descTimer.restart();
									break;
								}else{
									encontroCode++;
					  	   			updateTexto++;
					  	   			menuCode=1;
					  	   			gameState=2;
					  	   			update++;
					  	   			updateEncontro++;
					  	   			animalResgatado=false;
					  	   			t2=0;
					  	   			textoTimer.restart();
					  	   			encontroTimer.stop();
					  	   			descTimer.stop();
					  	   			break;
								}
							case 3:
								playSE(3);
								break;
							case 4:
									switch(fase) {
									case 0:
										playSE(3);
										break;
									case 2:
										playSE(6);
										fase++;
										frames=0;
										labelDesc.setText("");
										t2=0;
										encontroTexto="                    Boa! agora ele tem uma lata!";
										descTimer.restart();
										break;
									case 4:
										playSE(3);
										break;
									case 6:
										playSE(3);
										break;
									case 8:
										playSE(6);
										labelDesc.setText("");
										gameState=4;
										update++;
										break;
									}break;
							}										
							break;
						case 4:
							switch(encontroCode) {
							case 0:
								if(animalResgatado==false) {
									playSE(6);
									animalResgatado=true;
									stopMusic(7);
									updateOST=2;
									labelDesc.setText("");
									t2=0;
									encontroTexto="Parabéns! você ajudou a TARTARUGA, aperte qualquer tecla!";
									descTimer.restart();
									break;
								}else{
									encontroCode++;
					  	   			updateTexto++;
					  	   			menuCode=1;
					  	   			gameState=2;
					  	   			update++;
					  	   			updateEncontro++;
					  	   			animalResgatado=false;
					  	   			t2=0;
					  	   			textoTimer.restart();
					  	   			encontroTimer.stop();
					  	   			descTimer.stop();
					  	   			break;
								}
							case 1:
								playSE(3);
								break;
							case 2:
								playSE(3);
								break;
							case 3:
								playSE(3);
								break;
								
								case 4:
									switch(fase) {
									case 0:
										playSE(3);
										break;
									case 2:
										playSE(3);
										break;
									case 4:
										playSE(3);
										break;
									case 6:
										playSE(6);
										updateOST=3;
										fase++;
										frames=0;
										labelDesc.setText("");
										t2=0;
										descTimer.restart();
										encontroTexto="           ESSA NÃO! agora ele tem restos de comida?!";
										break;
									case 8:
										playSE(3);
										labelDesc.setText("");
										gameState=4;
										update++;
									}break;								
							}
							break;
						case 5:
							if (animalResgatado==false) {
							playSE(6);
							playSE(19);
							stopMusic(17);
							stopMusic(17); 
							fase++;
							frames=0;
							animalResgatado=true;
							labelDesc.setText("");
							t2=0;
							descTimer.restart();
							encontroTexto="";
							}
						}
						break;
				}break;
				
				case KeyEvent.VK_ESCAPE:
					playSE(20);
					System.exit(0);
	  	   			break;
				}
			
			}
			@Override
			public void keyTyped(KeyEvent e) {

			}
		}
	
public void playMusic(int i) {
		
		sound.setFile(i);
		sound.play(i);
		sound.loop(i);
	}
public void stopMusic(int i) {
		
		sound.stop(i);
	}
public void playSE(int i) {
		
		sound.setFile(i);
		sound.play(i);
	}
}