import matplotlib.pyplot as plt
import numpy as np

# Datos
frequencies = [-40, -20, 0, 20, 40]
amplitudes = [3, 0.5, 2, 0.5, 3]
fases = [-180, 20, 0, -20, 180]

# Gráfico de Amplitud
plt.figure(1)
plt.stem(frequencies, amplitudes, use_line_collection=True)
plt.title('Espectro de Línea de Amplitud')
plt.xlabel('Frecuencia (Hz)')
plt.ylabel('Amplitud')
plt.grid()

# Gráfico de Fase
plt.figure(2)
plt.stem(frequencies, fases, use_line_collection=True)
plt.title('Espectro de Línea de Fase')
plt.xlabel('Frecuencia (Hz)')
plt.ylabel('Fase (grados)')
plt.grid()

plt.show()