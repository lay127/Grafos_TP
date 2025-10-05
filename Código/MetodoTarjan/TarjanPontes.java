import java.util.*;
import java.io.*;

public class TarjanPontes {

    private List<int[]> pontes;

    public TarjanPontes(Grafo G) {
        int V = G.V();
        pontes = new ArrayList<>();
        boolean[] visitado = new boolean[V];
        int[] tempoDesc = new int[V];
        int[] menorTempo = new int[V];
        int contador = 0;

        // DFS iterativa com pilha
        for (int start = 0; start < V; start++) {
            if (!visitado[start]) {
                Stack<int[]> stack = new Stack<>();
                stack.push(new int[]{start, -1, 0}); // {v, pai, index do vizinho}

                int[] iterIndex = new int[V]; // índice do próximo vizinho a visitar

                while (!stack.isEmpty()) {
                    int[] topo = stack.peek();
                    int v = topo[0], pai = topo[1], i = topo[2];

                    if (!visitado[v]) {
                        visitado[v] = true;
                        tempoDesc[v] = menorTempo[v] = contador++;
                    }

                    List<Integer> adj = G.adjList(v);
                    if (i < adj.size()) {
                        int w = adj.get(i);
                        stack.peek()[2]++; // incrementa índice para próximo vizinho

                        if (w == pai) continue; // ignora o pai

                        if (!visitado[w]) {
                            stack.push(new int[]{w, v, 0});
                        } else {
                            menorTempo[v] = Math.min(menorTempo[v], tempoDesc[w]);
                        }
                    } else {
                        // todos os vizinhos foram processados
                        stack.pop();
                        if (pai != -1) {
                            menorTempo[pai] = Math.min(menorTempo[pai], menorTempo[v]);
                            if (menorTempo[v] > tempoDesc[pai]) {
                                pontes.add(new int[]{pai, v});
                            }
                        }
                    }
                }
            }
        }
    }

    public Iterable<int[]> pontes() {
        return pontes;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Insira o nome do arquivo: ");
        String filename = sc.nextLine();

        Grafo G = null;
        try {
            Scanner fileScanner = new Scanner(new File(filename));
            int n = fileScanner.nextInt();
            int m = fileScanner.nextInt();
            G = new Grafo(n);

            for (int i = 0; i < m; i++) {
                int v = fileScanner.nextInt() - 1;
                int w = fileScanner.nextInt() - 1;
                G.addEdge(v, w);
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Erro ao abrir o arquivo");
            System.exit(1);
        }

        TarjanPontes tarjan = new TarjanPontes(G);

        System.out.println("\nPontes encontradas:");
        for (int[] aresta : tarjan.pontes()) {
            System.out.println((aresta[0] + 1) + " - " + (aresta[1] + 1));
        }

        sc.close();
    }
}

/**
 * Classe Grafo mínima
 */
class Grafo {
    private int V;
    private List<Integer>[] adj;

    public Grafo(int V) {
        this.V = V;
        adj = new ArrayList[V];
        for (int i = 0; i < V; i++) {
            adj[i] = new ArrayList<>();
        }
    }

    public int V() {
        return V;
    }

    public void addEdge(int v, int w) {
        adj[v].add(w);
        adj[w].add(v);
    }

    public List<Integer> adjList(int v) {
        return adj[v];
    }
}
