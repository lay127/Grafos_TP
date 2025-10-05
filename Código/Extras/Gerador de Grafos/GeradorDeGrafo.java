import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/******************************************************************************
 *  Gerador de grafos simples em Java.
 *  Funciona sem dependências externas.
 *  Permite escolher tipo de grafo e salvar em arquivo TXT.
 ******************************************************************************/

public class GeradorDeGrafo {
    private static final class Edge implements Comparable<Edge> {
        private int v, w;

        private Edge(int v, int w) {
            if (v < w) {
                this.v = v; this.w = w;
            } else {
                this.v = w; this.w = v;
            }
        }

        public int compareTo(Edge that) {
            if (this.v != that.v) return this.v - that.v;
            return this.w - that.w;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Edge)) return false;
            Edge e = (Edge) o;
            return this.v == e.v && this.w == e.w;
        }

        @Override
        public int hashCode() {
            return Objects.hash(v, w);
        }
    }

    // =================== CLASSE GRAPH SIMPLIFICADA =========================
    static class Graph {
        private int V;
        private List<Integer>[] adj;

        public Graph(int V) {
            this.V = V;
            adj = new ArrayList[V];
            for (int i = 0; i < V; i++) adj[i] = new ArrayList<>();
        }

        public void addEdge(int v, int w) {
            adj[v].add(w);
            adj[w].add(v);
        }

        public int V() { return V; }

        public int E() {
            int count = 0;
            for (List<Integer> lista : adj) count += lista.size();
            return count / 2;
        }

        public Iterable<Integer> adj(int v) { return adj[v]; }
    }

    private static Random rand = new Random();

    // =================== MÉTODOS DE GERAÇÃO DE GRAFOS =========================

    public static Graph simple(int V, int E) {
        if (E > (long) V*(V-1)/2 || E < 0) 
            throw new IllegalArgumentException("Numero de arestas invalido");
        Graph G = new Graph(V);
        HashSet<Edge> set = new HashSet<>();
        while (G.E() < E) {
            int v = rand.nextInt(V);
            int w = rand.nextInt(V);
            Edge e = new Edge(v, w);
            if (v != w && !set.contains(e)) {
                set.add(e);
                G.addEdge(v, w);
            }
        }
        return G;
    }

    public static Graph complete(int V) {
        Graph G = new Graph(V);
        for (int v = 0; v < V; v++)
            for (int w = v+1; w < V; w++)
                G.addEdge(v, w);
        return G;
    }

    public static Graph cycle(int V) {
        Graph G = new Graph(V);
        int[] vertices = new int[V];
        for (int i = 0; i < V; i++) vertices[i] = i;
        shuffle(vertices);
        for (int i = 0; i < V-1; i++) G.addEdge(vertices[i], vertices[i+1]);
        G.addEdge(vertices[V-1], vertices[0]);
        return G;
    }

    public static Graph star(int V) {
        Graph G = new Graph(V);
        int[] vertices = new int[V];
        for (int i = 0; i < V; i++) vertices[i] = i;
        shuffle(vertices);
        for (int i = 1; i < V; i++) G.addEdge(vertices[0], vertices[i]);
        return G;
    }

    public static Graph binaryTree(int V) {
        Graph G = new Graph(V);
        int[] vertices = new int[V];
        for (int i = 0; i < V; i++) vertices[i] = i;
        shuffle(vertices);
        for (int i = 1; i < V; i++)
            G.addEdge(vertices[i], vertices[(i-1)/2]);
        return G;
    }

    public static Graph wheel(int V) {
        if (V <= 1) throw new IllegalArgumentException("Vertices >= 2");
        Graph G = new Graph(V);
        int[] vertices = new int[V];
        for (int i = 0; i < V; i++) vertices[i] = i;
        shuffle(vertices);

        for (int i = 1; i < V-1; i++) G.addEdge(vertices[i], vertices[i+1]);
        G.addEdge(vertices[V-1], vertices[1]);

        for (int i = 1; i < V; i++) G.addEdge(vertices[0], vertices[i]);
        return G;
    }

    public static Graph regular(int V, int k) {
        if (V*k % 2 != 0) throw new IllegalArgumentException("V*k deve ser par");
        Graph G = new Graph(V);
        int[] vertices = new int[V*k];
        for (int v = 0; v < V; v++)
            for (int j = 0; j < k; j++)
                vertices[v + V*j] = v;
        shuffle(vertices);
        for (int i = 0; i < V*k/2; i++)
            G.addEdge(vertices[2*i], vertices[2*i + 1]);
        return G;
    }

    public static Graph tree(int V) {
        Graph G = new Graph(V);
        if (V == 1) return G;

        int[] prufer = new int[V-2];
        for (int i = 0; i < V-2; i++) prufer[i] = rand.nextInt(V);

        int[] degree = new int[V];
        Arrays.fill(degree, 1);
        for (int x : prufer) degree[x]++;

        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int v = 0; v < V; v++)
            if (degree[v] == 1) pq.add(v);

        for (int i = 0; i < V-2; i++) {
            int v = pq.poll();
            G.addEdge(v, prufer[i]);
            degree[v]--; degree[prufer[i]]--;
            if (degree[prufer[i]] == 1) pq.add(prufer[i]);
        }
        int a = pq.poll(), b = pq.poll();
        G.addEdge(a, b);
        return G;
    }

    // =================== MÉTODO AUXILIAR =========================
    private static void shuffle(int[] arr) {
        for (int i = arr.length-1; i > 0; i--) {
            int j = rand.nextInt(i+1);
            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
    }

    // =================== MAIN =========================
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Digite o numero de vertices: ");
        int V = sc.nextInt();
        System.out.print("Digite o numero de arestas (se aplicavel): ");
        int E = sc.nextInt();

        System.out.println("Escolha o tipo de grafo:");
        System.out.println("1 - Simple");
        System.out.println("2 - Complete");
        System.out.println("3 - Cycle");
        System.out.println("4 - Star");
        System.out.println("5 - Binary Tree");
        System.out.println("6 - Wheel");
        System.out.println("7 - Regular 4");
        System.out.println("8 - Tree");
        int opcao = sc.nextInt();

        Graph G = null;
        switch (opcao) {
            case 1: G = simple(V, E); break;
            case 2: G = complete(V); break;
            case 3: G = cycle(V); break;
            case 4: G = star(V); break;
            case 5: G = binaryTree(V); break;
            case 6: G = wheel(V); break;
            case 7: G = regular(V, 4); break;
            case 8: G = tree(V); break;
            default:
                System.out.println("Opcao invalida!");
                System.exit(0);
        }

        // Define o caminho fixo
        String caminhoArquivo = "../../Código/Bases/Grafo.txt";

        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            writer.write(G.V() + " " + G.E() + "\n");
            for (int v = 0; v < G.V(); v++) {
                for (int w : G.adj(v)) {
                    if (v < w) writer.write(v + " " + w + "\n");
                }
            }
            System.out.println("✅ Grafo salvo em: " + caminhoArquivo);
        } catch (IOException ex) {
            System.out.println("Erro ao salvar o arquivo: " + ex.getMessage());
        }

        sc.close();
    }
}

// Fonte: https://algs4.cs.princeton.edu/40graphs/