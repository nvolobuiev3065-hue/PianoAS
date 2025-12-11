# Still D.R.E. by Dr. Dre ft. Snoop Dogg - Piano Intro with Chords
# Setup
LOADI $s1, 90          # Volume
LOADI $t1, 120         # Tempo
SETTEMPO $s2, $t1
LOADI $t3, 3           # Octave 3
SETOCTAVE $s3, $t3

main_riff:
# CEA pattern - 8 times
# C3(48), E4(64), A4(69)
LOADI $t1, 48          # C3
LOADI $t2, 64          # E4
LOADI $t3, 69          # A4

CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3

# BEA pattern - 3 times
# B2(47), E4(64), A4(69)
LOADI $t1, 47          # B2
LOADI $t2, 64          # E4
LOADI $t3, 69          # A4

CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3

# BEG pattern - 5 times
# B2(47), E4(64), G4(67)
LOADI $t1, 47          # B2
LOADI $t2, 64          # E4
LOADI $t3, 67          # G4

CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3

# Repeat the full pattern
repeat:
# CEA - 8 times
LOADI $t1, 48
LOADI $t2, 64
LOADI $t3, 69

CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3

# BEA - 3 times
LOADI $t1, 47
LOADI $t2, 64
LOADI $t3, 69

CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3

# BEG - 5 times
LOADI $t1, 47
LOADI $t2, 64
LOADI $t3, 67

CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3

repeat2:
# CEA - 8 times
LOADI $t1, 48
LOADI $t2, 64
LOADI $t3, 69

CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3

# BEA - 3 times
LOADI $t1, 47
LOADI $t2, 64
LOADI $t3, 69

CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3

# BEG - 5 times
LOADI $t1, 47
LOADI $t2, 64
LOADI $t3, 67

CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3

repeat3:
# CEA - 8 times
LOADI $t1, 48
LOADI $t2, 64
LOADI $t3, 69

CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3

# BEA - 3 times
LOADI $t1, 47
LOADI $t2, 64
LOADI $t3, 69

CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3

# BEG - 5 times
LOADI $t1, 47
LOADI $t2, 64
LOADI $t3, 67

CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3
CHORDS $t1, $t2, $t3