package mars.mips.instructions.customlangs;

import javax.sound.midi.*;
import mars.mips.hardware.*;
import mars.*;
import mars.util.*;
import mars.mips.instructions.*;

public class PianoAS extends CustomAssembly {
  @Override
  public String getName() {
    return "Piano Assembly";
  }

  @Override
  public String getDescription() {
    return "Assembly language to let your computer produce beautiful music";
  }

  @Override
  protected void populate() {
    instructionList.add(
        new BasicInstruction("MIX $t1,$t2,$t3",
            "Mixing : change the pitch by adding the values in $t2 and $t3 and storing the result in $t1",
            BasicInstructionFormat.R_FORMAT,
            "000000 fffff sssss ttttt 00000000001",
            new SimulationCode() {
              public void simulate(ProgramStatement statement) throws ProcessingException {
                try {
                  int[] operands = statement.getOperands();
                  int add1 = RegisterFile.getValue(operands[1]);
                  int add2 = RegisterFile.getValue(operands[2]);
                  int sum = add1 + add2;
                  RegisterFile.updateRegister(operands[0], sum);
                } catch (Exception e) {
                  SystemIO.printString("error: " + e.getMessage() + "\n");
                }

              }
            }));
    instructionList.add(
        new BasicInstruction("DIFF $t1,$t2,$t3",
            "Difference : lower the pitch by subtracting the value in $t3 from $t2 and storing the result in $t1",
            BasicInstructionFormat.R_FORMAT,
            "000000 fffff sssss ttttt 00000 100010",
            new SimulationCode() {
              public void simulate(ProgramStatement statement) throws ProcessingException {
                try {
                  int[] operands = statement.getOperands();
                  int base = RegisterFile.getValue(operands[1]);
                  int subtractor = RegisterFile.getValue(operands[2]);
                  int result = base - subtractor;
                  RegisterFile.updateRegister(operands[0], result);
                } catch (Exception e) {
                  SystemIO.printString("error: " + e.getMessage() + "\n");
                }
              }
            }));
    instructionList.add(
        new BasicInstruction("BLENDI $t1,12",
            "Blend Immediate : $t1 = $t1 + immediate value",
            BasicInstructionFormat.I_FORMAT,
            "000001 fffff 00000 sssssssssssssss",
            new SimulationCode() {
              public void simulate(ProgramStatement statement) throws ProcessingException {
                try {
                  int[] operands = statement.getOperands();
                  int base = RegisterFile.getValue(operands[0]);
                  int immediate = operands[1] << 16 >> 16;
                  int result = base + immediate;
                  if ((base >= 0 && immediate >= 0 && result < 0)
                      || (base < 0 && immediate < 0 && result >= 0)) {
                    throw new ProcessingException(statement, "Overflow");
                  }
                  RegisterFile.updateRegister(operands[0], result);
                } catch (Exception e) {
                  SystemIO.printString("error: " + e.getMessage() + "\n");
                }
              }
            }));
    instructionList.add(
        new BasicInstruction("SHIFT $t1,$t2,12",
            "Shift : used for changing the tempo by shifting the value in $t2 left or right by an immediate value",
            BasicInstructionFormat.I_FORMAT,
            "000010 fffff sssss iiiiiiiiiiiiiiii",
            new SimulationCode() {
              public void simulate(ProgramStatement statement) throws ProcessingException {
                try {
                  int[] operands = statement.getOperands();
                  int dest = operands[0];
                  int src = operands[1];
                  int immediate = operands[2] << 16 >> 16;

                  int srcValue = RegisterFile.getValue(src);

                  int result;
                  if (immediate >= 0) {
                    result = srcValue << immediate;
                  } else {
                    result = srcValue >> -immediate;
                  }
                  RegisterFile.updateRegister(dest, result);
                } catch (Exception e) {
                  SystemIO.printString("error: " + e.getMessage() + "\n");
                }
              }
            }));

    instructionList.add(
        new BasicInstruction("MODULATE $t1,$t2,$t3",
            "Modulate : $t1 = $t2 % $t3 (keep values in a range, 0–11 for chromatic scale)",
            BasicInstructionFormat.R_FORMAT,
            "000000 fffff sssss ttttt 00000 000011",
            new SimulationCode() {
              public void simulate(ProgramStatement statement) throws ProcessingException {
                try {
                  int[] operands = statement.getOperands();
                  int a = RegisterFile.getValue(operands[1]);
                  int b = RegisterFile.getValue(operands[2]);
                  if (b == 0) {
                    throw new ProcessingException(statement, "Error: division by zero");
                  }
                  int result = a % b;
                  RegisterFile.updateRegister(operands[0], result);
                } catch (Exception e) {
                  SystemIO.printString("error: " + e.getMessage() + "\n");
                }
              }
            }));

    instructionList.add(
        new BasicInstruction("MASK $t1,$t2,$t3",
            "Mask : Extract a specific part of a number that contains some musical setting (note, rhythm, and etc.)",
            BasicInstructionFormat.R_FORMAT,
            "000100 fffff sssss ttttt 00000 000001",
            new SimulationCode() {
              public void simulate(ProgramStatement statement) throws ProcessingException {
                try {
                  int[] operands = statement.getOperands();
                  int firstValue = RegisterFile.getValue(operands[1]);
                  int maskValue = RegisterFile.getValue(operands[2]);
                  int result = firstValue & maskValue;
                  RegisterFile.updateRegister(operands[0], result);
                } catch (Exception e) {
                  SystemIO.printString("error: " + e.getMessage() + "\n");
                }
              }
            }));
    instructionList.add(
        new BasicInstruction("MERGE $t1,$t2,$t3",
            "Merge : Combine specific parts of numbers that contain some musical settings (note, rhythm, and etc.)",
            BasicInstructionFormat.R_FORMAT,
            "000100 fffff sssss ttttt 00000 000011",
            new SimulationCode() {
              public void simulate(ProgramStatement statement) throws ProcessingException {
                try {
                  int[] operands = statement.getOperands();
                  int firstValue = RegisterFile.getValue(operands[1]);
                  int secondValue = RegisterFile.getValue(operands[2]);
                  int result = firstValue | secondValue;
                  RegisterFile.updateRegister(operands[0], result);
                } catch (Exception e) {
                  SystemIO.printString("error: " + e.getMessage() + "\n");
                }

              }
            }));
    instructionList.add(
        new BasicInstruction("FLIP $t1,$t2,$t3",
            "Flip : Invert specific parts of numbers that contain some musical settings (note, rhythm, and etc.)",
            BasicInstructionFormat.R_FORMAT,
            "000100 fffff sssss ttttt 00000 000100",
            new SimulationCode() {
              public void simulate(ProgramStatement statement) throws ProcessingException {
                try {
                  int[] operands = statement.getOperands();
                  int firstValue = RegisterFile.getValue(operands[1]);
                  int secondValue = RegisterFile.getValue(operands[2]);
                  int result = firstValue ^ secondValue;
                  RegisterFile.updateRegister(operands[0], result);
                } catch (Exception e) {
                  SystemIO.printString("error: " + e.getMessage() + "\n");
                }
              }
            }));
    instructionList.add(
        new BasicInstruction("COPY $s0,$t1",
            "Copy : Copy a number that contains some musical setting like pitch to pitch register",
            BasicInstructionFormat.R_FORMAT,
            "000100 fffff sssss 00000 00000 000110",
            new SimulationCode() {
              public void simulate(ProgramStatement statement) throws ProcessingException {
                try {
                  int[] operands = statement.getOperands();
                  int firstValue = RegisterFile.getValue(operands[1]);
                  RegisterFile.updateRegister(operands[0], firstValue);
                } catch (Exception e) {
                  SystemIO.printString("error: " + e.getMessage() + "\n");
                }
              }
            }));
    instructionList.add(
        new BasicInstruction("JUMPIFEQ $t1,$t2",
            "Jump if Equal : Jump to a specific instruction if the values in two registers are equal",
            BasicInstructionFormat.R_FORMAT,
            "000100 fffff sssss 00000 000000000110",
            new SimulationCode() {
              public void simulate(ProgramStatement statement) throws ProcessingException {
                try {
                  int[] operands = statement.getOperands();
                  int firstValue = RegisterFile.getValue(operands[0]);
                  int secondValue = RegisterFile.getValue(operands[1]);
                  if (firstValue == secondValue) {
                    Globals.instructionSet.processJump(operands[2]);
                  }
                } catch (Exception e) {
                  SystemIO.printString("error: " + e.getMessage() + "\n");
                }
              }
            }));
    // instructionList.add(
    // new BasicInstruction("JUMP label",
    // "Jump : unconditionally jump to label",
    // BasicInstructionFormat.I_BRANCH_FORMAT,
    // "000101 00000 00000 fffffffffffffff",
    // new SimulationCode() {
    // public void simulate(ProgramStatement st) throws ProcessingException {
    // int[] ops = st.getOperands();
    // Globals.instructionSet.processJump(ops[0]);
    // }
    // }));

    // 10 Creative instructions
    instructionList.add(
        new BasicInstruction("SETPITCH $s0,$t1",
            "Set Pitch : Set the pitch register to a specific value",
            BasicInstructionFormat.R_FORMAT,
            "010000 fffff sssss 00000 000000000000",
            new SimulationCode() {
              public void simulate(ProgramStatement statement) throws ProcessingException {
                try {
                  int[] operands = statement.getOperands();
                  int pitchValue = RegisterFile.getValue(operands[1]);

                  RegisterFile.updateRegister(operands[0], pitchValue);
                } catch (Exception e) {
                  SystemIO.printString("error: " + e.getMessage() + "\n");
                }

              }
            }));
    instructionList.add(
        new BasicInstruction("SETVOLUME $s1,$t1",
            "Set Volume : Set the volume register to a specific value",
            BasicInstructionFormat.R_FORMAT,
            "010001 fffff sssss 00000 000000000000",
            new SimulationCode() {
              public void simulate(ProgramStatement statement) throws ProcessingException {
                try {
                  int[] operands = statement.getOperands();
                  int pitchValue = RegisterFile.getValue(operands[1]);

                  RegisterFile.updateRegister(operands[0], pitchValue);
                } catch (Exception e) {
                  SystemIO.printString("error: " + e.getMessage() + "\n");
                }
              }
            }));
    instructionList.add(
        new BasicInstruction("SETTEMPO $s2,$t1",
            "Set Tempo : Set the tempo register to a specific value",
            BasicInstructionFormat.R_FORMAT,
            "010010 fffff sssss 00000 000000000000",
            new SimulationCode() {
              public void simulate(ProgramStatement statement) throws ProcessingException {
                try {
                  int[] operands = statement.getOperands();
                  int pitchValue = RegisterFile.getValue(operands[1]);

                  RegisterFile.updateRegister(operands[0], pitchValue);
                } catch (Exception e) {
                  SystemIO.printString("error: " + e.getMessage() + "\n");
                }
              }
            }));
    instructionList.add(
        new BasicInstruction("SETOCTAVE $s3,$t1",
            "Set Octave : Set the octave register to a specific value",
            BasicInstructionFormat.R_FORMAT,
            "010011 fffff sssss 00000 000000000000",
            new SimulationCode() {
              public void simulate(ProgramStatement statement) throws ProcessingException {
                try {
                  int[] operands = statement.getOperands();
                  int pitchValue = RegisterFile.getValue(operands[1]);

                  RegisterFile.updateRegister(operands[0], pitchValue);
                } catch (Exception e) {
                  SystemIO.printString("error: " + e.getMessage() + "\n");
                }
              }
            }));
    instructionList.add(
        new BasicInstruction("PLAY",
            "Play: Sends values of s0 and s1 to the audio output device",
            BasicInstructionFormat.R_FORMAT,
            "100000 00000 00000 00000 000000000000",
            new SimulationCode() {
              Synthesizer synth = null;
              MidiChannel channel = null;

              public void simulate(ProgramStatement statement) throws ProcessingException {
                try {
                  if (synth == null) {
                    synth = MidiSystem.getSynthesizer();
                    synth.open();
                    channel = synth.getChannels()[0];
                    channel.programChange(0);
                  }

                  int pitch = RegisterFile.getValue(16);
                  int volume = RegisterFile.getValue(17);
                  int tempo = RegisterFile.getValue(18);
                  int octave = RegisterFile.getValue(19);

                  int effectivePitch = pitch + (octave * 12);
                  int durationMs = (int) (60000.0 / tempo);

                  channel.noteOn(effectivePitch, volume);
                  Thread.sleep(durationMs);
                  channel.noteOff(effectivePitch);

                } catch (Exception e) {
                  SystemIO.printString("error: " + e.getMessage() + "\n");
                }
              }
            }));
    instructionList.add(
        new BasicInstruction("PLAYR $t1,$t2",
            "Play: Plays sound using $t1 as pitch and $t2 as volume",
            BasicInstructionFormat.R_FORMAT,
            "100000 fffff sssss 00000 000000000000",
            new SimulationCode() {
              Synthesizer synth = null;
              MidiChannel channel = null;

              public void simulate(ProgramStatement statement) throws ProcessingException {
                try {
                  if (synth == null) {
                    synth = MidiSystem.getSynthesizer();
                    synth.open();
                    channel = synth.getChannels()[0];
                    channel.programChange(0);
                  }
                  int[] operands = statement.getOperands();
                  int pitch = RegisterFile.getValue(operands[0]);
                  int volume = RegisterFile.getValue(operands[1]);
                  int tempo = RegisterFile.getValue(10);

                  channel.noteOn(pitch, volume);

                  int durationMs = (int) (60000.0 / tempo);
                  Thread.sleep(durationMs);

                  channel.noteOff(pitch);

                } catch (Exception e) {
                  SystemIO.printString("error: " + e.getMessage() + "\n");
                }
              }
            }));
    instructionList.add(
        new BasicInstruction("REST 2",
            "Rest : pause playback for a number of beats",
            BasicInstructionFormat.I_FORMAT,
            "100001 00000 00000 ffffffffffffffff",
            new SimulationCode() {
              public void simulate(ProgramStatement statement) throws ProcessingException {
                try {
                  int[] operands = statement.getOperands();
                  int imm = operands[0] << 16 >> 16;
                  int tempo = RegisterFile.getValue(17);

                  long duration = (60000L * imm) / tempo;
                  Thread.sleep(duration);
                } catch (Exception e) {
                  SystemIO.printString("error: " + e.getMessage() + "\n");
                }

              }
            }));

    instructionList.add(
        new BasicInstruction("CHORD $t1,$t2,$t3",
            "Chord : play three pitches stored in $t1, $t2, and $t3 simultaneously",
            BasicInstructionFormat.R_FORMAT,
            "100010 fffff sssss ttttt 00000 000000",
            new SimulationCode() {

              Synthesizer synth = null;
              MidiChannel channel = null;

              public void simulate(ProgramStatement statement) throws ProcessingException {

                try {
                  if (synth == null) {
                    synth = MidiSystem.getSynthesizer();
                    synth.open();
                    channel = synth.getChannels()[0];
                    channel.programChange(0);
                  }

                  int[] ops = statement.getOperands();
                  int p1 = RegisterFile.getValue(ops[0]);
                  int p2 = RegisterFile.getValue(ops[1]);
                  int p3 = RegisterFile.getValue(ops[2]);

                  int volume = RegisterFile.getValue(9);
                  int tempo = RegisterFile.getValue(10);

                  channel.noteOn(p1, volume);
                  channel.noteOn(p2, volume);
                  channel.noteOn(p3, volume);

                  int durationMs = (int) (60000.0 / tempo);
                  Thread.sleep(durationMs);

                  channel.noteOff(p1);
                  channel.noteOff(p2);
                  channel.noteOff(p3);

                } catch (Exception e) {
                  SystemIO.printString("error: " + e.getMessage() + "\n");
                }
              }
            }));
    instructionList.add(
        new BasicInstruction("GLISS $t1,$t2",
            "Gliss : smoothly play every pitch between $t1 and $t2",
            BasicInstructionFormat.R_FORMAT,
            "100011 fffff sssss 00000 00000 000000",
            new SimulationCode() {

              Synthesizer synth = null;
              MidiChannel channel = null;

              public void simulate(ProgramStatement statement) throws ProcessingException {

                try {
                  if (synth == null) {
                    synth = MidiSystem.getSynthesizer();
                    synth.open();
                    channel = synth.getChannels()[0];
                    channel.programChange(0);
                  }

                  int[] ops = statement.getOperands();
                  int start = RegisterFile.getValue(ops[0]);
                  int end = RegisterFile.getValue(ops[1]);

                  int volume = RegisterFile.getValue(9);
                  int tempo = RegisterFile.getValue(10);

                  int step = (end >= start) ? 1 : -1;

                  int glideSpeed = (int) (60000.0 / tempo / 8);

                  for (int p = start; p != end + step; p += step) {
                    channel.noteOn(p, volume);
                    Thread.sleep(glideSpeed);
                    channel.noteOff(p);
                  }

                } catch (Exception e) {
                  SystemIO.printString("error: " + e.getMessage() + "\n");
                }
              }
            }));
    instructionList.add(
        new BasicInstruction("RANDOMP",
            "Random Pitch : generates a random pitch 0–127 and stores it in P0",
            BasicInstructionFormat.R_FORMAT,
            "100100 00000 00000 00000 00000 000000",
            new SimulationCode() {
              public void simulate(ProgramStatement statement) throws ProcessingException {
                int[] operands = statement.getOperands();
                int pitch = new java.util.Random().nextInt(128);
                RegisterFile.updateRegister(operands[0], pitch);
              }
            }));
    instructionList.add(
        new BasicInstruction("LOADI $t1,120",
            "Load Immediate : Load an immediate value into $t1",
            BasicInstructionFormat.I_FORMAT,
            "100110 00000 fffff ssssssssssssssss",
            new SimulationCode() {
              public void simulate(ProgramStatement statement) throws ProcessingException {
                try {
                  int[] operands = statement.getOperands();
                  int immediate = operands[1] << 16 >> 16;
                  RegisterFile.updateRegister(operands[0], immediate);
                } catch (Exception e) {
                  SystemIO.printString("error: " + e.getMessage() + "\n");
                }
              }
            }));
    instructionList.add(
        new BasicInstruction("CHORDS $t1,$t2,$t3",
            "Chord : play three pitches stored in $t1, $t2, and $t3 simultaneously",
            BasicInstructionFormat.R_FORMAT,
            "100010 fffff sssss ttttt 00000 000000",
            new SimulationCode() {

              Synthesizer synth = null;
              MidiChannel channel = null;

              public void simulate(ProgramStatement statement) throws ProcessingException {

                try {
                  if (synth == null) {
                    synth = MidiSystem.getSynthesizer();
                    synth.open();
                    channel = synth.getChannels()[0];
                    channel.programChange(0);
                  }

                  int[] ops = statement.getOperands();
                  int p1 = RegisterFile.getValue(ops[0]);
                  int p2 = RegisterFile.getValue(ops[1]);
                  int p3 = RegisterFile.getValue(ops[2]);

                  int volume = RegisterFile.getValue(9);
                  int tempo = RegisterFile.getValue(10);

                  channel.noteOn(p1, volume);
                  channel.noteOn(p2, volume);
                  channel.noteOn(p3, volume);

                  int durationMs = (int) (48000.0 / tempo / 3);
                  Thread.sleep(durationMs);

                  channel.noteOff(p1);
                  channel.noteOff(p2);
                  channel.noteOff(p3);

                } catch (Exception e) {
                  SystemIO.printString("error: " + e.getMessage() + "\n");
                }
              }
            }));
  }
}