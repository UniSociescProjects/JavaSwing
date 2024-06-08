package modelos;
import java.util.Scanner;

public class PesquisaBinaria extends Pesquisa {
		
	    	public static (String[] args) {
	        int[] arr = {1, 3, 6, 8, 10, 12, 15, 18, 20}; 

	        Scanner scanner = new Scanner(System.in);
	        System.out.print("Digite o número para procurar: ");
	        int target = scanner.nextInt();

	        int resultado = binarySearch(arr, target);

	        if (resultado != -1) {
	            System.out.println("Elemento encontrado: " + resultado);
	        } else {
	            System.out.println("Elemento não encontrado.");
	        }
	    }

	    public static int binarySearch(int[] arr, int target) {
	        int left = 0;
	        int right = arr.length - 1;

	        while (left <= right) {
	            int mid = left + (right - left) / 2;

	            if (arr[mid] == target) {
	                return mid; 
	            } else if (arr[mid] < target) {
	                left = mid + 1; 
	            } else {
	                right = mid - 1; 
	            }
	        }

	        return -1; 
	    }
	}

}
