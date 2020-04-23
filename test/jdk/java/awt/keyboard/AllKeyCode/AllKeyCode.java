/*
 * Copyright (c) 2016, 2017, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/**
 * @test
 * @key headful
 * @bug 8149456 8147834 8150230 8155740 8163265
 * @requires os.family == "mac"
 * @summary Tests key codes for all keys supported in Java for Mac OS X.
 * @run main AllKeyCode
 */

import java.awt.AWTException;
import java.awt.Frame;
import java.awt.Robot;
import java.awt.TextArea;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AllKeyCode extends Frame {

    private static final int PAUSE =  2000;

    private static Frame frame;
    private static TextArea textArea;
    private static KeyListener keyListener;
    private static int allKeyArr[];
    private static volatile int keyPressed;
    private static CountDownLatch keyPressedLatch;
    private static volatile boolean allKeysWerePressed = true;
    private static volatile boolean keyCodesAreCorrect = true;

    AllKeyCode() {
        AllKeyCode.allKeyArr = new int[] {
            KeyEvent.VK_BACK_SPACE,
            KeyEvent.VK_TAB,
            KeyEvent.VK_ENTER,
            KeyEvent.VK_CLEAR,
            KeyEvent.VK_SHIFT,
            KeyEvent.VK_CONTROL,
            KeyEvent.VK_ALT,
            KeyEvent.VK_CAPS_LOCK,
            KeyEvent.VK_ESCAPE,
            KeyEvent.VK_SPACE,
            KeyEvent.VK_PAGE_UP,
            KeyEvent.VK_PAGE_DOWN,
            KeyEvent.VK_END,
            KeyEvent.VK_HOME,
            KeyEvent.VK_LEFT,
            KeyEvent.VK_UP,
            KeyEvent.VK_RIGHT,
            KeyEvent.VK_DOWN,
            KeyEvent.VK_COMMA,
            KeyEvent.VK_MINUS,
            KeyEvent.VK_PERIOD,
            KeyEvent.VK_SLASH,
            KeyEvent.VK_0,
            KeyEvent.VK_1,
            KeyEvent.VK_2,
            KeyEvent.VK_3,
            KeyEvent.VK_4,
            KeyEvent.VK_5,
            KeyEvent.VK_6,
            KeyEvent.VK_7,
            KeyEvent.VK_8,
            KeyEvent.VK_9,
            KeyEvent.VK_SEMICOLON,
            KeyEvent.VK_EQUALS,
            KeyEvent.VK_A,
            KeyEvent.VK_B,
            KeyEvent.VK_C,
            KeyEvent.VK_D,
            KeyEvent.VK_E,
            KeyEvent.VK_F,
            KeyEvent.VK_G,
            KeyEvent.VK_H,
            KeyEvent.VK_I,
            KeyEvent.VK_J,
            KeyEvent.VK_K,
            KeyEvent.VK_L,
            KeyEvent.VK_M,
            KeyEvent.VK_N,
            KeyEvent.VK_O,
            KeyEvent.VK_P,
            KeyEvent.VK_Q,
            KeyEvent.VK_R,
            KeyEvent.VK_S,
            KeyEvent.VK_T,
            KeyEvent.VK_U,
            KeyEvent.VK_V,
            KeyEvent.VK_W,
            KeyEvent.VK_X,
            KeyEvent.VK_Y,
            KeyEvent.VK_Z,
            KeyEvent.VK_OPEN_BRACKET,
            KeyEvent.VK_BACK_SLASH,
            KeyEvent.VK_CLOSE_BRACKET,
            KeyEvent.VK_NUMPAD0,
            KeyEvent.VK_NUMPAD1,
            KeyEvent.VK_NUMPAD2,
            KeyEvent.VK_NUMPAD3,
            KeyEvent.VK_NUMPAD4,
            KeyEvent.VK_NUMPAD5,
            KeyEvent.VK_NUMPAD6,
            KeyEvent.VK_NUMPAD7,
            KeyEvent.VK_NUMPAD8,
            KeyEvent.VK_NUMPAD9,
            KeyEvent.VK_MULTIPLY,
            KeyEvent.VK_ADD,
            KeyEvent.VK_SUBTRACT,
            KeyEvent.VK_DECIMAL,
            KeyEvent.VK_DIVIDE,
            KeyEvent.VK_F1,
            KeyEvent.VK_F2,
            KeyEvent.VK_F3,
            KeyEvent.VK_F4,
            KeyEvent.VK_F5,
            KeyEvent.VK_F6,
            KeyEvent.VK_F7,
            KeyEvent.VK_F8,
            KeyEvent.VK_F9,
            KeyEvent.VK_F10,
            // F11 is mapped to "Show Desktop" macOS system shortcut by default, so skip it
            // KeyEvent.VK_F11,
            KeyEvent.VK_F12,
            KeyEvent.VK_DELETE,
            // MacOS handles the event corresponding to HELP and this affects the next key pressing
            // KeyEvent.VK_HELP,
            KeyEvent.VK_META,
            KeyEvent.VK_BACK_QUOTE,
            KeyEvent.VK_QUOTE,
            KeyEvent.VK_F13,
            // MacOS handles events corresponding to F14/15 and does not send them to runtime in case of non-Apple keyboard
            // KeyEvent.VK_F14,
            // KeyEvent.VK_F15,
            KeyEvent.VK_F16,
            KeyEvent.VK_F17,
            KeyEvent.VK_F18,
            KeyEvent.VK_F19,
            KeyEvent.VK_F20,
            KeyEvent.VK_ALT_GRAPH
        };

        keyPressed = -1;
    }

    private void createAndShowGUI() {
        frame = new Frame("Function Key Keycodes");
        textArea = new TextArea();
        textArea.setFocusable(true);
        frame.add(textArea);
        frame.pack();
        frame.setSize(200, 200);

        textArea.addKeyListener(keyListener = new KeyListener() {

            @Override
            public void keyTyped(KeyEvent ke) {
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                if (keyPressed != ke.getKeyCode()) {
                    // MacOS doesn't distinguish between ALT and ALT_GRAPH keys,
                    // and JetBrains runtime also does not, please see JBR-1108 for more info.
                    // Here we have incompatibility between JetBrains and OpenJDK runtime,
                    // as OpenJDK generates different key codes for these keys.
                    if ((keyPressed != KeyEvent.VK_ALT_GRAPH) || (ke.getKeyCode() != KeyEvent.VK_ALT)) {
                        System.err.println("Wrong keycode received: " + keyPressed + " != " + ke.getKeyCode());
                        keyCodesAreCorrect = false;
                    }
                }
                keyPressedLatch.countDown();
            }

            @Override
            public void keyReleased(KeyEvent ke) {
            }
        });
        frame.setVisible(true);
        textArea.requestFocus();
    }

    private void removeListener() {
        if (keyListener != null) {
            textArea.removeKeyListener(keyListener);
            keyListener = null;
        }
    }

    @Override
    public void dispose() {
        if (null != frame) {
            frame.dispose();
            frame = null;
        }
    }

    public void generateFunctionKeyPress() throws InterruptedException {
        try {
            Robot robot = new Robot();
            robot.waitForIdle();

            for (int i = 0; i < allKeyArr.length; i++) {
                keyPressedLatch = new CountDownLatch(1);
                keyPressed = allKeyArr[i];
                robot.keyPress(keyPressed);
                robot.keyRelease(keyPressed);
                robot.waitForIdle();
                if(!keyPressedLatch.await(PAUSE, TimeUnit.MILLISECONDS)) {
                    System.err.println("Key was not pressed: " + keyPressed);
                    allKeysWerePressed = false;
                };
            }
            removeListener();

        } catch (AWTException e) {
            throw new RuntimeException("Robot creation failed");
        }
    }

    public static void main(String args[]) throws InterruptedException {
        AllKeyCode allKeyObj = new AllKeyCode();
        allKeyObj.createAndShowGUI();
        Thread.sleep(PAUSE);
        allKeyObj.generateFunctionKeyPress();
        Thread.sleep(PAUSE);
        allKeyObj.dispose();

        if(keyCodesAreCorrect && allKeysWerePressed) {
            System.out.println("Test Passed");
        } else {
            throw new RuntimeException("Test Failed");
        }
    }
}
