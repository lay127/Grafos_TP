import java.util.*;
import java.io.*;

public class MetodoTarjan {

    // ----------------- CLASSE GRAFO -----------------
    static class Grafo {
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

    // ----------------- TARJAN -----------------
    static class Tarjan {
        private List<int[]> pontes;

        public Tarjan(Grafo G) {
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
    }

    // ----------------- FLEURY -----------------
    // Remove a aresta u-v do grafo
    static void removeEdge(List<Integer>[] adj, int u, int v) {
        adj[u].remove(Integer.valueOf(v));
        adj[v].remove(Integer.valueOf(u));
    }

    // Verifica se a aresta u-v é ponte usando Tarjan
    static boolean isValidNextEdge(int u, int v, Grafo G, List<Integer>[] adj) {
        // Se u tem apenas uma aresta restante, esta aresta deve ser usada
        if (adj[u].size() == 1) return true;

        // Copia o grafo temporariamente
        Grafo tempG = new Grafo(G.V());
        for (int i = 0; i < G.V(); i++) {
            tempG.adj[i] = new ArrayList<>(adj[i]);
        }

        removeEdge(tempG.adj, u, v); // Remove a aresta temporariamente

        Tarjan tarjan = new Tarjan(tempG);

        // Se u-v está entre as pontes restantes, não é válida
        for (int[] ponte : tarjan.pontes()) {
            if ((ponte[0] == u && ponte[1] == v) || (ponte[0] == v && ponte[1] == u)) {
                return false;
            }
        }
        return true;
    }

    // Função recursiva que percorre o caminho Euleriano
    static void printEulerUtil(int u, Grafo G, List<Integer>[] adj) {
        for (int i = 0; i < adj[u].size(); i++) {
            int v = adj[u].get(i);
            if (isValidNextEdge(u, v, G, adj)) {
                System.out.println((u + 1) + " - " + (v + 1));
                removeEdge(adj, u, v);
                printEulerUtil(v, G, adj);
                break; // sai do loop após usar a aresta
            }
        }
    }

    // Função que inicia o caminho Euleriano
    static void printEulerPath(Grafo G) {
        int start = 0;
        int oddCount = 0;
        for (int i = 0; i < G.V(); i++) {
            if (G.adjList(i).size() % 2 != 0) {
                oddCount++;
                start = i;
            }
        }
        if (oddCount != 0 && oddCount != 2) {
            System.out.println("Existe caminho Euleriano: NÃO");
            return;
        }

        // Copia adj para não modificar original
        @SuppressWarnings("unchecked")
        List<Integer>[] adjCopy = new ArrayList[G.V()];
        for (int i = 0; i < G.V(); i++) adjCopy[i] = new ArrayList<>(G.adjList(i));

        System.out.println("Existe caminho Euleriano: SIM");
        printEulerUtil(start, G, adjCopy);
    }

    // ----------------- MAIN -----------------
    public static void main(String[] args) {
        long tempoInicial = System.currentTimeMillis();
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

        // Mostra as pontes usando Tarjan
        Tarjan tarjan = new Tarjan(G);
        System.out.println("\nPontes encontradas:");
        for (int[] aresta : tarjan.pontes()) {
            System.out.println((aresta[0] + 1) + " - " + (aresta[1] + 1));
        }

        // Executa Fleury
        System.out.println();
        printEulerPath(G);

        sc.close();
        long tempoFinal = System.currentTimeMillis();
        System.out.println("O metodo foi executado em " + (tempoFinal - tempoInicial) + " ms");
    }
}