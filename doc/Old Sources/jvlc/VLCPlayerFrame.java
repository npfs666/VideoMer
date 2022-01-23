package test.jvlc;

/*****************************************************************************
 * Based on VlcClient.java from VideoLAN team
 *****************************************************************************
 * Copyright (C) 1998-2006 the VideoLAN team
 * This program is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 * 
 */
import java.awt.Canvas;
import java.awt.Frame;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import test.jvlc.core.JVLC;
import test.jvlc.core.MediaListPlayer;
import test.jvlc.core.VLCException;


public class VLCPlayerFrame extends Frame {

  private MediaListPlayer playlist;
  public Canvas jvcanvas;

  public VLCPlayerFrame(String[] args) {
    initComponents(args);
  }

  private void initComponents(String[] args) {

    java.awt.GridBagConstraints gridBagConstraints;

    fullScreenButton = new javax.swing.JButton();
    jTextField1 = new javax.swing.JTextField();
    setButton = new javax.swing.JButton();
    pauseButton = new javax.swing.JButton();
    stopButton = new javax.swing.JButton();

    jvcc = new JPanel();
    jvcanvas = new java.awt.Canvas();
    jvcanvas.setSize(200, 200);
    jvcc.add(jvcanvas);

    String[] arg = new String[4];
    arg[0] = "--intf=dummy ";
    arg[1] = "--ignore-config ";
    arg[2] = "--no-plugins-cache ";
    arg[3] = "--plugin-path=plugins";

    jvlc = new JVLC(arg);

    playlist = new MediaListPlayer(jvlc);

    setLayout(new java.awt.GridBagLayout());

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridwidth = java.awt.GridBagConstraints.CENTER;
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    add(jvcc, gridBagConstraints);

    fullScreenButton.setText("FullScreen");
    fullScreenButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        fullScreenButtonActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    // FIXME it's not work 
    //add(fullScreenButton, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    add(jTextField1, gridBagConstraints);

    setButton.setText("Browse");
    setButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        //setButtonActionPerformed(evt);
        showFileChooser();
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    add(setButton, gridBagConstraints);

    pauseButton.setText("Play/Pause");
    pauseButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        pauseButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    add(pauseButton, gridBagConstraints);

    stopButton.setText("Stop");
    stopButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        stopButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    add(stopButton, gridBagConstraints);

    pack();

  }

  private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {
    try {
      playlist.stop();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void pauseButtonActionPerformed(java.awt.event.ActionEvent evt) {
    try {
      //playlist.togglePause();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setButtonActionPerformed(java.awt.event.ActionEvent evt) {
   /* try {
      jvlc.setVideoOutput(jvcanvas);
      playlist.add(jTextField1.getText(), "a.avi");
      playlist.play();
    } catch (VLCException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }*/
  }

  private void fullScreenButtonActionPerformed(java.awt.event.ActionEvent evt) {
    // TODO not works, need check through jna
    // jvlc.fullScreen();
  }

  void showFileChooser() {
    JFileChooser chooser = new JFileChooser();

    int returnVal = chooser.showOpenDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      jvlc.setVideoOutput(jvcanvas);
      try {
        jTextField1.setText(chooser.getSelectedFile().getAbsolutePath());
        //playlist.add(chooser.getSelectedFile().getAbsolutePath(), "selected file");
        playlist.play();
      } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.OK_OPTION);
      }
    }
  }
  private javax.swing.JButton setButton;
  private javax.swing.JButton pauseButton;
  private javax.swing.JButton stopButton;
  private javax.swing.JButton fullScreenButton;
  private javax.swing.JTextField jTextField1;
  private JPanel jvcc;
  public JVLC jvlc;
  // MediaControlInstance mci;
}
