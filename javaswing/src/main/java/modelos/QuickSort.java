package modelos;

public class QuickSort extends Ordenacao {
	public static void quicksort(long[] array, int menor, int maior) {
		if (menor<maior) {
			// PARTICIONAR O ARRAY E OBTER O INDICE DO PIVO
			int p = particao(array, menor, maior);
			
			//RECURSIVAMENTE ORDENAR OS SUB-ARRAYS
			quicksort(array, menor, p - 1); // SUB-ARRAY À ESQUERDA DO PIVO
            quicksort(array, p + 1, maior); // SUB-ARRAY À DIREITA DO PIVO
		}
	}
	
	// METODO DE PARTICAO DO ARRAY
	public static int particao(long[] array, int menor, int maior) {
		int pivo = (int)array[maior]; // ESCOLHE O ULTIMO ELEMENTO COMO PIVO
        int i = (menor - 1); // INDICE DO MENOR ELEMENTO
        
        for (int j = menor; j < maior; j++) {
            // SE O ELEMENTO ATUAL É MENOR OU IGUAL AO PIVO
            if (array[j] <= pivo) {
                i++;
                // TROCAR array[i] E array[j]
                int temp = (int)array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        
        // TROCAR array[i+1] E array[maior] (OU O PIVO)
        int temp = (int)array[i + 1];
        array[i + 1] = array[maior];
        array[maior] = temp;
        return i + 1;
	}
}
