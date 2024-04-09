import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.Flow;

import javax.swing.border.Border;

//hello

public class FrameDemo implements ActionListener
{
public Frame f = new Frame("Notepad");
MenuItem blue,yellow,red,plus, minus, i7,italic, bold;
int size=13;
String path;
Stack<String> dataStack;

//list to store all the occurrences of the search
ArrayList<Integer> occurrences = new ArrayList<>();
int currIndex = -1;

public TextArea editArea = new TextArea();
	public void actionPerformed(ActionEvent e){
			if(e.getSource() == yellow)
				editArea.setForeground(Color.YELLOW);
			else if(e.getSource() == red)
				editArea.setForeground(Color.RED);

			else if(e.getSource() == plus){
				size = size+7;
				Font f = new Font("Javanese Text", Font.PLAIN, size);
				editArea.setFont(f);
			}
			else if(e.getSource() == minus){
				size = size-2;
				Font f =new Font("Javanese Text", Font.PLAIN, size);
				editArea.setFont(f);
			}
			else if(e.getSource() == i7){
				//System.exit(0);
				exitApplication();
			}
			else if(e.getSource() == italic){
				Font f = new Font("Javanese Text", Font.ITALIC, size);
				editArea.setFont(f);
			}else if (e.getSource() == bold){
				Font f = new Font("Javanese Text", Font.BOLD, size);
				editArea.setFont(f);
			}
			
			else 
				editArea.setForeground(Color.BLUE);
			
		}
	
	public FrameDemo() {
		//text area textListener to track the changes and update stack
		dataStack = new Stack<>();
		editArea.addTextListener(new TextListener() {
            public void textValueChanged(TextEvent e) {
                pushToDataStack();
            }

			private void pushToDataStack() {
				dataStack.push(editArea.getText());
				System.out.println(dataStack.peek());
			}
        });

		

		//Menubar

		MenuBar mb = new MenuBar();
		Menu m = new Menu("File \t");
		
		

		MenuItem i1 = new MenuItem("New");
			///adding the action to the i1 menu item which clears the text area
			i1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					editArea.setText("");
					path = null;
				}
			
			});
			//end of i1 action


		MenuItem i2 = new MenuItem("Open");
			///adding the action to the i2 menu item which opens a file 
			i2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					FileDialog fd = new FileDialog(f, "Open", FileDialog.LOAD);
					fd.setVisible(true);
					path = fd.getDirectory() + fd.getFile();
					System.out.println(path);

					//reading the file content
					try(BufferedReader reader = new BufferedReader(new FileReader(path))){
						StringBuilder content = new StringBuilder();
						String line;
						while((line = reader.readLine()) != null){
							content.append(line).append("\n");
						}
						editArea.setText(content.toString());
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}
			});
			//end of i2 action

		MenuItem i3 = new MenuItem("Save");
			///adding the action to the i3 menu item which saves the content of the text area to a file
			i3.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					if(path != null) {
						try(BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
							writer.write(editArea.getText());
						}catch(Exception ex){
							ex.printStackTrace();
						}

					}else {
						FileDialog fd = new FileDialog(f, "save", FileDialog.SAVE);
						fd.setVisible(true);

						path = fd.getDirectory() + fd.getFile();
						try(BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
							writer.write(editArea.getText());
						}catch(Exception ex){
							ex.printStackTrace();
						}
					}
				}
			});
			//end of i3 action
			
		MenuItem i4 = new MenuItem("SaveAs");
			///adding the action to the i4 menu item which saves the content of the text area to a file	
			i4.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					FileDialog fd = new FileDialog(f, "save", FileDialog.SAVE);
					fd.setVisible(true);

					path = fd.getDirectory() + fd.getFile();
					if (path != null) {
						try(BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
							writer.write(editArea.getText());
						}catch(Exception ex){
							ex.printStackTrace();
						}
					}
					
				}
			});

		MenuItem line = new MenuItem("__________________");
		MenuItem i5 = new MenuItem("Page setup");
			///adding the action to the i5 menu item which opens a dialog box for page setup
			i5.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					showPageSetupDialog();
				}
			});
			//end of i5 action
		MenuItem i6 = new MenuItem("Print");
			///add action to the i6 menu item which prints the content of the text area
		MenuItem line2 = new MenuItem("__________________");
		MenuItem i7 = new MenuItem("Exit");
		i7.addActionListener(this);

		m.add(i1);
		m.add(i2);
		m.add(i3);
		m.add(i4);
		m.add(line);
		m.add(i5);
		m.add(i6);
		m.add(line2);
		m.add(i7);
		
		

		//Part 2 Edit
		Menu edit = new Menu("Edit \t");
		MenuItem e1 = new MenuItem("Undo     Cltr+z");
			///adding the action to the e1 menu item which undoes the last action
			e1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					undo();
				}
			
			});
			//end of e1 action
				///adding the 'cltr+z' key press option to undo the last action
				editArea.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z){
							undo();
						}
					}
				});
				//end of 'cltr+z' key press option
			
		MenuItem line3 = new MenuItem("_____________");
		edit.add(line3);
		edit.add(e1);


		MenuItem e2 = new MenuItem("Cut      Cltr+x");
		edit.add(e2);
				///adding the action to the e2 menu item which cuts the selected text
				e2.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						cut();
					}
				});
				//end of e2 action
					///adding the 'cltr+x' key press option to cut the selected text
					editArea.addKeyListener((new KeyAdapter() {
						@Override
						public void keyPressed(KeyEvent e) {
							if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_X){
								cut();
							}
						}
					}));
					//end of 'cltr+x' key press option

		MenuItem e3 = new MenuItem("Copy");
		edit.add(e3);
				///adding the action to the e3 menu item which copies the selected text
				e3.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						copy();
					}
				});
				//end of e3 action
					///adding the 'cltr+c' key press option to copy the selected text
					editArea.addKeyListener((new KeyAdapter() {
						@Override
						public void keyPressed(KeyEvent e) {
							if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_C){
								copy();
							}
						}
					}));
					//end of 'cltr+c' key press option
		
		MenuItem e4 = new MenuItem("Paste");
		edit.add(e4);
					///adding the action to the e4 menu item which pastes the selected text
					e4.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							paste();
						}
					});
					//end of e4 action
						///adding the 'cltr+v' key press option to paste the seledted text
						editArea.addKeyListener(new KeyAdapter() {
							@Override
							public void keyPressed(KeyEvent e) {
								if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V){
									paste();
								}
							}
						});

		MenuItem del = new MenuItem("Delete");
		edit.add(del);
						///adding the action to the del menu item which deletes the selected text
						del.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								delete();
							}
						});
						//end del action
							///adding the 'delete' key press option to delete the selected text
							editArea.addKeyListener(new KeyAdapter() {
								@Override
								public void keyPressed(KeyEvent e) {
									if(e.getKeyCode() == KeyEvent.VK_DELETE){
										delete();
									}
								}
							});
							//end of 'delete' key press option
	
		MenuItem line4 = new MenuItem("___________");
		edit.add(line4);
		MenuItem e5 = new MenuItem("Find");
		edit.add(e5);
					///adding the action to the e5 menu item which finds the text in the text area
					e5.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							showFindDialog();
						}
					});
		MenuItem e6 = new MenuItem("Find Next");
		edit.add(e6);
		MenuItem e7 = new MenuItem("Find previous");
		edit.add(e7);
		MenuItem e8 = new MenuItem("Replace");
		edit.add(e8);
		MenuItem e9 = new MenuItem("Go To");
		edit.add(e9);
		MenuItem line5 = new MenuItem("____________");
		edit.add(line5);
		MenuItem e10 = new MenuItem("Select all");
		edit.add(e10);
		MenuItem e11 = new MenuItem("Time/Date");
		edit.add(e11);
		MenuItem line6 = new MenuItem("____________");
		edit.add(line6);
		Menu font = new Menu("Font");
		Menu fColor = new Menu("Font Color");
		 blue = new MenuItem("Blue");
		fColor.add(blue);
		blue.addActionListener(this);

		 yellow = new MenuItem("Yellow");
		fColor.add(yellow);
		yellow.addActionListener(this);

		 red = new MenuItem("Red");
		fColor.add(red);
		red.addActionListener(this);

		font.add(fColor);
		
		Menu size = new Menu("Font size");
		 plus = new MenuItem("+");
		size.add(plus);
		plus.addActionListener(this);

		minus = new MenuItem("--");
		size.add(minus);
		minus.addActionListener(this);
		font.add(size);

		Menu fFamily = new Menu("Font Style");
		 italic = new MenuItem("Italic");
		fFamily.add(italic);
		italic.addActionListener(this);
		 bold = new MenuItem("Bold");
		fFamily.add(bold);
		bold.addActionListener(this);
		font.add(fFamily);
		
		
		
		edit.add(font);
		//view
		Menu view = new Menu("View");
		MenuItem v1 = new MenuItem("Zoom");
		view.add(v1);
		MenuItem v2 = new MenuItem("Status Bar");
		view.add(v2);
		MenuItem v3 = new MenuItem("Word wrap");
		view.add(v3);
		
		
		mb.add(m);
		mb.add(edit);
		mb.add(view);

		f.setMenuBar(mb);
		//Menubar end
		Panel mainPanel = new Panel(new BorderLayout());

		f.add(mainPanel);
		mainPanel.add(editArea, BorderLayout.CENTER);
		
		f.setVisible(true);
		f.setSize(700,700);
		f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }

			
        });	
	}

	private void exitApplication() {
		f.dispose();
	}

	private void showPageSetupDialog() {
        Dialog dialog = new Dialog(f, "Page Setup", true);
        dialog.setLayout(new GridLayout(5, 2));

        Label orientationLabel = new Label("Orientation:");
        Choice orientationChoice = new Choice();
        orientationChoice.add("Portrait");
        orientationChoice.add("Landscape");

        Label paperSizeLabel = new Label("Paper Size:");
        Choice paperSizeChoice = new Choice();
        paperSizeChoice.add("A4");
        paperSizeChoice.add("Letter");

        Label marginsLabel = new Label("Margins (inches):");
        TextField topMarginField = new TextField("1.0");
        TextField bottomMarginField = new TextField("1.0");
        TextField leftMarginField = new TextField("1.0");
        TextField rightMarginField = new TextField("1.0");

        Button okButton = new Button("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Apply the selected page setup options
                applyPageSetup(orientationChoice.getSelectedItem(),
                               paperSizeChoice.getSelectedItem(),
                               topMarginField.getText(),
                               bottomMarginField.getText(),
                               leftMarginField.getText(),
                               rightMarginField.getText());
                dialog.dispose();
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        dialog.add(orientationLabel);
        dialog.add(orientationChoice);
        dialog.add(paperSizeLabel);
        dialog.add(paperSizeChoice);
        dialog.add(marginsLabel);
        dialog.add(topMarginField);
        dialog.add(new Label("")); // Empty label for alignment
        dialog.add(bottomMarginField);
        dialog.add(leftMarginField);
        dialog.add(rightMarginField);
        dialog.add(okButton);
        dialog.add(cancelButton);

        dialog.setSize(300, 200);
        dialog.setVisible(true);
    }

    private void applyPageSetup(String orientation, String paperSize, String topMargin, String bottomMargin, String leftMargin, String rightMargin) {
        // Placeholder for applying page setup options
        System.out.println("Orientation: " + orientation);
        System.out.println("Paper Size: " + paperSize);
        System.out.println("Margins - Top: " + topMargin + ", Bottom: " + bottomMargin + ", Left: " + leftMargin + ", Right: " + rightMargin);

		//apply the page setup
		
    }

	private void undo() {
		if(!dataStack.isEmpty()){
			String previousText = dataStack.pop();
			editArea.setText(previousText);
			//editArea.setText(dataStack.peek());
		}
	}
	
	private void cut() {
		String selectedText = editArea.getSelectedText();
		if(selectedText!=null && !selectedText.isEmpty()) {
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			StringSelection selection = new StringSelection(selectedText);
			clipboard.setContents(selection, null);

			int start = editArea.getSelectionStart();
			int end = editArea.getSelectionEnd();
			editArea.replaceRange("", start, end);
		}
	}
	
	private void copy() {
		String selectedText = editArea.getSelectedText();
		if(selectedText != null && !selectedText.isEmpty()) {
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			StringSelection selection = new StringSelection(selectedText);
			clipboard.setContents(selection, null);
		}
	}

	private void paste() {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		if(clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
			try{
				String clipboardText = (String) clipboard.getData(DataFlavor.stringFlavor);
				editArea.replaceRange(clipboardText, editArea.getSelectionStart(), editArea.getSelectionEnd());
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	private void delete() {
		int start = editArea.getSelectionStart();
		int end = editArea.getSelectionEnd();
		editArea.replaceRange("", start, end);
	}

	private void showFindDialog() {
		Panel findPanel = new Panel(new FlowLayout(FlowLayout.RIGHT));
		TextField findField = new TextField(20);
			findPanel.add(new Label("Find:"));
			findPanel.add(findField);
			Button findButton = new Button("Find");
			findButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String text = findField.getText();
					if(!text.isEmpty()) {
						occurrences.clear();
						currIndex = -1;
						findText(text, occurrences);
						navigate(0, findField.getText());
						editArea.requestFocus();
					}
				}
			});
			findPanel.add(findButton);

			Button closeButton = new Button("Close");
			closeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					f.remove(findPanel);
					f.validate();
					
				}
			});
			findPanel.add(closeButton);

			Button backButton = new Button("Back");
			backButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					navigate(-1, findField.getText());
					editArea.requestFocus();
				}
			});
			findPanel.add(backButton);
	
			Button nextButton = new Button("Next");
			nextButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					navigate(1, findField.getText());
					editArea.requestFocus();
				}
			});
			findPanel.add(nextButton);
	
			f.add(findPanel, BorderLayout.NORTH);
			f.validate(); // Refresh the layout to show the new panel
	
		// Dialog findDialog = new Dialog(f, "Find", true);
		// findDialog.setLayout(new GridLayout(2, 2));
		// Label findLabel = new Label("Find:");
		// TextField findField = new TextField(20);
		// Button findButton = new Button("find");
		// Button cancleButton = new Button("cancle");

		// Button backButton = new Button("Back");
        // Button forwardButton = new Button("Forward");

		

		// findButton.addActionListener(new ActionListener() {
		// 	@Override
		// 	public void actionPerformed(ActionEvent e) {
		// 		String text = findField.getText();
		// 		if(!text.isEmpty()) {
		// 			occurrences.clear();
		// 			currIndex = -1;
		// 			findText(text, occurrences);
		// 			navigate(0, findField.getText());
		// 			editArea.requestFocus();
		// 		}
				
		// 	}
		// });

		// cancleButton.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent e) {
        //         findDialog.dispose();
        //     }
        // });

		// backButton.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent e) {
        //         navigate(-1, findField.getText()); // Move backward
        //     }
        // });

        // forwardButton.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent e) {
        //         navigate(1, findField.getText()); // Move forward
        //     }
        // });

		// Panel panel = new Panel(new FlowLayout());
        // panel.add(findLabel);
        // panel.add(findField);
        // panel.add(findButton);
        // panel.add(cancleButton);
		// panel.add(backButton);
        // panel.add(forwardButton);

        // findDialog.add(panel);
        // findDialog.pack();
        // findDialog.setLocationRelativeTo(f);
        // findDialog.setVisible(true);
	}

	private void findText(String text, ArrayList<Integer> occurrences) {
		String content = editArea.getText();
		int index = content.indexOf(text);
		editArea.select(index, index+text.length());
		while(index != -1) {
			occurrences.add(index);
			index = content.indexOf(text, index + 1);
		}



		if(occurrences.isEmpty()) {
			showMessageDialog("Find", "Text not found");
		}
	}
	public void navigate(int direction, String searchText) {
			if(direction == -1 && currIndex > 0) {
				currIndex--;
			}else if(direction == 1 && currIndex < occurrences.size() - 1) {
				currIndex++;
			}

			if(currIndex >= 0 && currIndex < occurrences.size()) {
				int start = occurrences.get(currIndex);
				editArea.select(start, start+searchText.length());
			}
	}
	private void showMessageDialog(String title, String message) {
        Dialog messageDialog = new Dialog(f, title, true);
        Label messageLabel = new Label(message);
        Button okButton = new Button("OK");

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                messageDialog.dispose();
            }
        });

        Panel panel = new Panel(new FlowLayout());
        panel.add(messageLabel);
        panel.add(okButton);
		

        messageDialog.add(panel);
        messageDialog.pack();
        messageDialog.setLocationRelativeTo(f);
        messageDialog.setVisible(true);
    }


	public static void main(String args[]) 
	{
		new FrameDemo();	
	}

	
}
