
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
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Example {

  public static void main(String[] args) {
    System.out.println(new java.io.File("").getAbsolutePath());
    String[] arg = new String[1];
    arg[0] = new java.io.File("").getAbsolutePath();
    Frame vlcFrame = new VLCPlayerFrame(arg);
    vlcFrame.setBounds(0, 0, 500, 500);
    vlcFrame.addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosing(WindowEvent ev) {
        //System.out.println("exit");
        System.exit(0);
      }
    });
    vlcFrame.setVisible(true);
  }
}