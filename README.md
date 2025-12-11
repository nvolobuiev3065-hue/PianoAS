PianoAS is a custom programming language for making piano music using simple MIPS related instructions. The problem my solution solves is the lack of musicalal output in the original Mars program. 
My language allows you to create music of different levels of difficulty. I was able to successfully recreate songs ranging from Twinkle, Twinkle to Still D.R.E Snoop Dogg and Dr.Dre. Under the hood, it uses Java library 
javax.sound.midi which allows to synthesize MIDI (Musical Instrument Digital Interface) data. The final set of instructions differs from the intially shipped one and here is the list of them with their description:

## Reserved Registers

The following registers have special purposes in Piano Assembly:

| Register | Number | Purpose |
|----------|--------|---------|
| `$s0` | 16 | Pitch register - stores the current pitch value (MIDI note 0-127) |
| `$s1` | 17 | Volume register - stores the current volume/velocity (0-127) |
| `$s2` | 18 | Tempo register - stores the tempo in beats per minute (BPM) |
| `$s3` | 19 | Octave register - stores the octave offset (multiplied by 12 and added to pitch) |

**Note:** General purpose registers (`$t0-$t9`, `$s4-$s7`, etc.) can be used freely for calculations and temporary storage.

## Instruction Set

#### `LOADI $t1, 120`
**Purpose:** Load an immediate value into a register  
**Format:** `LOADI <register>, <immediate value>`  
**Example:**
```assembly
LOADI $t1, 60      # Load pitch value 60 (Middle C)
LOADI $s1, 80      # Load volume 80
LOADI $t2, 120     # Load tempo 120 BPM
```
**Use Case:** Initialize registers with constant values like MIDI notes, volume levels, or tempo settings.

---

#### `SETPITCH $s0, $t1`
**Purpose:** Set the pitch register to a specific value  
**Format:** `SETPITCH $s0, <source register>`  
**Registers:**
- `$s0` (register 16): Destination - pitch register
- Source register: Contains the MIDI note value (0-127)

**Example:**
```assembly
LOADI $t1, 60
SETPITCH $s0, $t1   # Set pitch to Middle C (60)
```

---

#### `SETVOLUME $s1, $t1`
**Purpose:** Set the volume register to a specific value  
**Format:** `SETVOLUME $s1, <source register>`  
**Registers:**
- `$s1` (register 17): Destination - volume register
- Source register: Contains the volume level (0-127, where 127 is loudest)

**Example:**
```assembly
LOADI $t1, 80
SETVOLUME $s1, $t1  # Set volume to 80
```

---

#### `SETTEMPO $s2, $t1`
**Purpose:** Set the tempo register to a specific value  
**Format:** `SETTEMPO $s2, <source register>`  
**Registers:**
- `$s2` (register 18): Destination - tempo register
- Source register: Contains the tempo in BPM

**Example:**
```assembly
LOADI $t1, 120
SETTEMPO $s2, $t1   # Set tempo to 120 BPM
```

---

#### `SETOCTAVE $s3, $t1`
**Purpose:** Set the octave register to a specific value  
**Format:** `SETOCTAVE $s3, <source register>`  
**Registers:**
- `$s3` (register 19): Destination - octave register
- Source register: Contains the octave number (typically 0-10)

**Example:**
```assembly
LOADI $t3, 4
SETOCTAVE $s3, $t3  # Set octave to 4 (middle range)
```
**Note:** The effective pitch = pitch + (octave × 12)

---

### Arithmetic Instructions

#### `MIX $t1, $t2, $t3`
**Purpose:** Add two values together (useful for transposing notes)  
**Format:** `MIX <dest>, <source1>, <source2>`  
**Operation:** `$t1 = $t2 + $t3`  
**Example:**
```assembly
LOADI $t2, 60      # Middle C
LOADI $t3, 7       # Perfect fifth interval
MIX $t1, $t2, $t3  # Result: 67 (G)
```
**Use Case:** Transpose notes, add intervals, combine pitch values.

---

#### `DIFF $t1, $t2, $t3`
**Purpose:** Subtract one value from another (useful for lowering pitch)  
**Format:** `DIFF <dest>, <source1>, <source2>`  
**Operation:** `$t1 = $t2 - $t3`  
**Example:**
```assembly
LOADI $t2, 69      # A note
LOADI $t3, 2       # Lower by 2 semitones
DIFF $t1, $t2, $t3 # Result: 67 (G)
```
**Use Case:** Lower pitch, calculate intervals, move down the scale.

---

#### `BLENDI $t1, 12`
**Purpose:** Add an immediate value to a register (in-place pitch adjustment)  
**Format:** `BLENDI <register>, <immediate value>`  
**Operation:** `$t1 = $t1 + immediate`  
**Example:**
```assembly
LOADI $t1, 60      # Start at Middle C
BLENDI $t1, 7      # Move up 7 semitones to G (67)
BLENDI $t1, 2      # Move up 2 more to A (69)
```
**Use Case:** Quick pitch adjustments, stepping through scales, transposing melodies.

---

#### `SHIFT $t1, $t2, 12`
**Purpose:** Bit shift left or right (useful for tempo changes)  
**Format:** `SHIFT <dest>, <source>, <shift amount>`  
**Operation:**
- If shift amount ≥ 0: `$t1 = $t2 << shift_amount` (left shift / multiply)
- If shift amount < 0: `$t1 = $t2 >> (-shift_amount)` (right shift / divide)

**Example:**
```assembly
LOADI $t2, 60      # Tempo 60 BPM
SHIFT $t1, $t2, 1  # Double tempo to 120 BPM (left shift by 1 = ×2)
SHIFT $t1, $t2, -1 # Half tempo to 30 BPM (right shift by 1 = ÷2)
```

---

#### `MODULATE $t1, $t2, $t3`
**Purpose:** Calculate modulo (keep values in a range, useful for chromatic scale)  
**Format:** `MODULATE <dest>, <dividend>, <divisor>`  
**Operation:** `$t1 = $t2 % $t3`  
**Example:**
```assembly
LOADI $t2, 15      # Note value
LOADI $t3, 12      # Chromatic scale (12 notes)
MODULATE $t1, $t2, $t3  # Result: 3 (wraps to valid note)
```
**Use Case:** Keep note values within octave (0-11), create cyclic patterns.

---

### Bitwise Instructions

#### `MASK $t1, $t2, $t3`
**Purpose:** Extract specific bits using AND operation  
**Format:** `MASK <dest>, <value>, <mask>`  
**Operation:** `$t1 = $t2 & $t3`  
**Example:**
```assembly
LOADI $t2, 0xFF    # Value with multiple bits
LOADI $t3, 0x0F    # Mask to keep only lower 4 bits
MASK $t1, $t2, $t3 # Result: 0x0F
```
**Use Case:** Extract note information from packed data, isolate specific musical parameters.

---

#### `MERGE $t1, $t2, $t3`
**Purpose:** Combine values using OR operation  
**Format:** `MERGE <dest>, <value1>, <value2>`  
**Operation:** `$t1 = $t2 | $t3`  
**Example:**
```assembly
LOADI $t2, 0x0F    # Lower bits
LOADI $t3, 0xF0    # Upper bits
MERGE $t1, $t2, $t3 # Result: 0xFF
```
**Use Case:** Combine musical settings, pack multiple parameters into one value.

---

#### `FLIP $t1, $t2, $t3`
**Purpose:** Toggle bits using XOR operation  
**Format:** `FLIP <dest>, <value1>, <value2>`  
**Operation:** `$t1 = $t2 ^ $t3`  
**Example:**
```assembly
LOADI $t2, 0xFF
LOADI $t3, 0x0F
FLIP $t1, $t2, $t3  # Toggle lower 4 bits
```
**Use Case:** Invert musical parameters, create variations.

---

### Data Movement Instructions

#### `COPY $s0, $t1`
**Purpose:** Copy a value from one register to another  
**Format:** `COPY <dest>, <source>`  
**Operation:** `dest = source`  
**Example:**
```assembly
LOADI $t1, 60
COPY $s0, $t1       # Copy pitch value to pitch register
```
**Use Case:** Move calculated values to special registers, backup values, prepare for playback.

---

### Playback Instructions

#### `PLAY`
**Purpose:** Play a note using values from the special registers  
**Format:** `PLAY
