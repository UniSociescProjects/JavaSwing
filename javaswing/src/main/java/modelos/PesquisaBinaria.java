package modelos;
import java.util.Scanner;

public class PesquisaBinaria extends Pesquisa {
	
	    public static int binarySearch(int[] array, int target) {
	        int left = 0;
	        int right = array.length - 1;

	        while (left <= right) {
	            int mid = left + (right - left) / 2;

	            if (array[mid] == target) {
	                return mid;
	            }

	            if (array[mid] < target) {
	                left = mid + 1;
	            } else {
	         
	                right = mid - 1;
	            }
	        }

	        return -1;
	    }

	    public static void main(String[] args) {
	        Scanner scanner = new Scanner(System.in);

	        System.out.print("Digite o tamanho do array: ");
	        int size = scanner.nextInt();

	        int[] array = new int[size];
	        System.out.println("Digite os elementos do array em ordem crescente:");
	        for (int i = 0; i < size; i++) {
	            array[i] = scanner.nextInt();
	        }

	        System.out.print("Digite o elemento alvo a ser pesquisado: ");
	        int target = scanner.nextInt();

	        int result = binarySearch(array, target);
	        if (result == -1) {
	            System.out.println("Elemento não encontrado no array");
	        } else {
	            System.out.println("Elemento encontrado no índice: " + result);
	        }

	        scanner.close();
	    }
	}
		
	    	
