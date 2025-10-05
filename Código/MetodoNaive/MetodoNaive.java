import java.util.*;
import java.io.*;

// ----------------- CLASSE GRAFO -----------------
class Graph {
    private int V;
    private List<Integer>[] adj;

    @SuppressWarnings("unchecked")
    public Graph(int V) {
        this.V = V;
        adj = new ArrayList[V + 1]; // mantido de acordo com Naive
        for (int i = 0; i <= V; i++) adj[i] = new ArrayList<>();
    }

    public int V() { return V; }

    public void addEdge(int v, int w) {
        adj[v].add(w);
        adj[w].add(v);
    }

    public void removerAresta(int v, int w) {
        adj[v].remove(Integer.valueOf(w));
        adj[w].remove(Integer.valueOf(v));
    }

    public List<Integer> adj(int v) { return adj[v]; }

    public List<Integer>[] getAdjList() { return adj; }
}

// ----------------- BUSCA EM PROFUNDIDADE / NAIVE -----------------
class BuscaEmProfundidade {
    private boolean[] marcado; // Confirma se um vertice ja foi marcado ou n
    private int count; // Contador global da busca

    BuscaEmProfundidade(Graph g){
        this.marcado = new boolean[g.V() + 1];
    }

    private void dfs(Graph g, int v){
        count++;
        marcado[v] = true;
        for (Integer w : g.adj(v)) {
            if(!marcado[w]) { dfs(g, w); }
        }
    }

    // Método para encontrar pontes (Naive)
    public List<List<Integer>> encontrarPontes(Graph G){
        List<List<Integer>> pontes = new ArrayList<>();
        List<List<Integer>> arestas = new ArrayList<>();

        for (int v = 1; v <= G.V(); v++) {
            for (int w : G.adj(v)) {
                if (v < w) arestas.add(Arrays.asList(v, w));
            }
        }

        for (List<Integer> aresta : arestas) {
            int v = aresta.get(0);
            int w = aresta.get(1);
            G.removerAresta(v, w);

            Arrays.fill(marcado, false);
            dfs(G, v);

            if (!marcado[w]) pontes.add(aresta);

            G.addEdge(v, w);
        }

        return pontes;
    }

    public boolean[] getMarcado() { return marcado; }
}

// ----------------- FLEURY -----------------
class Fleury {

    // Remove a aresta u-v do grafo
    static void removeEdge(Graph G, int u, int v) {
        G.removerAresta(u, v);
    }

    // Verifica se a aresta u-v pode ser removida usando o método Naive
    static boolean isValidNextEdge(Graph G, int u, int v) {
        if (G.adj(u).size() == 1) return true;

        // Testa se u-v é ponte usando Naive
        BuscaEmProfundidade dfs = new BuscaEmProfundidade(G);
        removeEdge(G, u, v);
        List<List<Integer>> pontes = dfs.encontrarPontes(G);
        G.addEdge(u, v);

        for (List<Integer> p : pontes) {
            if ((p.get(0) == u && p.get(1) == v) || (p.get(0) == v && p.get(1) == u)) return false;
        }
        return true;
    }

    // Função recursiva que percorre o caminho Euleriano
    static void printEulerUtil(Graph G, int u) {
        for (int v : new ArrayList<>(G.adj(u))) {
            if (isValidNextEdge(G, u, v)) {
                System.out.println(u + " - " + v);
                removeEdge(G, u, v);
                printEulerUtil(G, v);
                break; // usa apenas uma aresta de cada vez
            }
        }
    }

    // Função que inicia o caminho Euleriano
    static void printEulerPath(Graph G) {
        int start = 1;
        int oddCount = 0;
        for (int i = 1; i <= G.V(); i++) {
            if (G.adj(i).size() % 2 != 0) {
                oddCount++;
                start = i;
            }
        }

        if (oddCount != 0 && oddCount != 2) {
            System.out.println("Existe caminho Euleriano: NÃO");
            return;
        }

        System.out.println("Existe caminho Euleriano: SIM");
        printEulerUtil(G, start);
    }
}

// ----------------- MAIN -----------------
public class MetodoNaive {
    public static void main(String[] args) throws Exception {
        long tempoInicial = System.currentTimeMillis();
        Scanner sc = new Scanner(System.in);
        System.out.print("Insira o nome do arquivo: ");
        File arquivo = new File(sc.nextLine());

        Graph grafo = null;

        try {
            sc = new Scanner(arquivo);
            int n = sc.nextInt();
            int m = sc.nextInt();
            grafo = new Graph(n);
            for (int i = 0; i < m; i++) {
                int origem = sc.nextInt();
                int destino = sc.nextInt();
                grafo.addEdge(origem, destino);
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }

        if (grafo != null) {
            BuscaEmProfundidade dfs = new BuscaEmProfundidade(grafo);
            List<List<Integer>> pontes = dfs.encontrarPontes(grafo);
            System.out.println("Pontes no Grafo: " + pontes);

            System.out.println("\nCaminho Euleriano:");
            Fleury.printEulerPath(grafo);
        }

        sc.close();
        long tempoFinal = System.currentTimeMillis();
        System.out.println("O metodo foi executado em " + (tempoFinal - tempoInicial) + " ms");
    }
}