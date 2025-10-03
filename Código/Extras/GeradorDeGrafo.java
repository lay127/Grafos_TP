import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/******************************************************************************
 *  Compilation:  javac GraphGenerator.java
 *  Execution:    java GraphGenerator
 *  Dependencies: Graph.java, StdRandom.java, SET.java, MinPQ.java
 *
 *  A graph generator.
 *
 *  Adapted to let user choose type of graph and save it to a txt file.
 ******************************************************************************/


public class GraphGenerator {
    private static final class Edge implements Comparable<Edge> {
        private int v;
        private int w;

        private Edge(int v, int w) {
            if (v < w) {
                this.v = v;
                this.w = w;
            }
            else {
                this.v = w;
                this.w = v;
            }
        }

        public int compareTo(Edge that) {
            if (this.v < that.v) return -1;
            if (this.v > that.v) return +1;
            if (this.w < that.w) return -1;
            if (this.w > that.w) return +1;
            return 0;
        }
    }

    // this class cannot be instantiated
    private GraphGenerator() { }

    // =================== MÉTODOS ORIGINAIS =========================
    
    public static Graph simple(int V, int E) {
        if (E > (long) V*(V-1)/2) throw new IllegalArgumentException("Too many edges");
        if (E < 0)                throw new IllegalArgumentException("Too few edges");
        Graph G = new Graph(V);
        SET<Edge> set = new SET<Edge>();
        while (G.E() < E) {
            int v = StdRandom.uniformInt(V);
            int w = StdRandom.uniformInt(V);
            Edge e = new Edge(v, w);
            if ((v != w) && !set.contains(e)) {
                set.add(e);
                G.addEdge(v, w);
            }
        }
        return G;
    }

    public static Graph simple(int V, double p) {
        if (p < 0.0 || p > 1.0)
            throw new IllegalArgumentException("Probability must be between 0 and 1");
        Graph G = new Graph(V);
        for (int v = 0; v < V; v++)
            for (int w = v+1; w < V; w++)
                if (StdRandom.bernoulli(p))
                    G.addEdge(v, w);
        return G;
    }

    public static Graph complete(int V) {
        return simple(V, 1.0);
    }

    public static Graph completeBipartite(int V1, int V2) {
        return bipartite(V1, V2, V1*V2);
    }

    public static Graph bipartite(int V1, int V2, int E) {
        if (E > (long) V1*V2) throw new IllegalArgumentException("Too many edges");
        if (E < 0)            throw new IllegalArgumentException("Too few edges");
        Graph G = new Graph(V1 + V2);

        int[] vertices = new int[V1 + V2];
        for (int i = 0; i < V1 + V2; i++)
            vertices[i] = i;
        StdRandom.shuffle(vertices);

        SET<Edge> set = new SET<Edge>();
        while (G.E() < E) {
            int i = StdRandom.uniformInt(V1);
            int j = V1 + StdRandom.uniformInt(V2);
            Edge e = new Edge(vertices[i], vertices[j]);
            if (!set.contains(e)) {
                set.add(e);
                G.addEdge(vertices[i], vertices[j]);
            }
        }
        return G;
    }

    public static Graph bipartite(int V1, int V2, double p) {
        if (p < 0.0 || p > 1.0)
            throw new IllegalArgumentException("Probability must be between 0 and 1");
        int[] vertices = new int[V1 + V2];
        for (int i = 0; i < V1 + V2; i++)
            vertices[i] = i;
        StdRandom.shuffle(vertices);
        Graph G = new Graph(V1 + V2);
        for (int i = 0; i < V1; i++)
            for (int j = 0; j < V2; j++)
                if (StdRandom.bernoulli(p))
                    G.addEdge(vertices[i], vertices[V1+j]);
        return G;
    }

    public static Graph path(int V) {
        Graph G = new Graph(V);
        int[] vertices = new int[V];
        for (int i = 0; i < V; i++)
            vertices[i] = i;
        StdRandom.shuffle(vertices);
        for (int i = 0; i < V-1; i++) {
            G.addEdge(vertices[i], vertices[i+1]);
        }
        return G;
    }

    public static Graph binaryTree(int V) {
        Graph G = new Graph(V);
        int[] vertices = new int[V];
        for (int i = 0; i < V; i++)
            vertices[i] = i;
        StdRandom.shuffle(vertices);
        for (int i = 1; i < V; i++) {
            G.addEdge(vertices[i], vertices[(i-1)/2]);
        }
        return G;
    }

    public static Graph cycle(int V) {
        Graph G = new Graph(V);
        int[] vertices = new int[V];
        for (int i = 0; i < V; i++)
            vertices[i] = i;
        StdRandom.shuffle(vertices);
        for (int i = 0; i < V-1; i++) {
            G.addEdge(vertices[i], vertices[i+1]);
        }
        G.addEdge(vertices[V-1], vertices[0]);
        return G;
    }

    public static Graph wheel(int V) {
        if (V <= 1) throw new IllegalArgumentException("Number of vertices must be at least 2");
        Graph G = new Graph(V);
        int[] vertices = new int[V];
        for (int i = 0; i < V; i++)
            vertices[i] = i;
        StdRandom.shuffle(vertices);

        for (int i = 1; i < V-1; i++) {
            G.addEdge(vertices[i], vertices[i+1]);
        }
        G.addEdge(vertices[V-1], vertices[1]);

        for (int i = 1; i < V; i++) {
            G.addEdge(vertices[0], vertices[i]);
        }

        return G;
    }

    public static Graph star(int V) {
        if (V <= 0) throw new IllegalArgumentException("Number of vertices must be at least 1");
        Graph G = new Graph(V);
        int[] vertices = new int[V];
        for (int i = 0; i < V; i++)
            vertices[i] = i;
        StdRandom.shuffle(vertices);

        for (int i = 1; i < V; i++) {
            G.addEdge(vertices[0], vertices[i]);
        }

        return G;
    }

    public static Graph regular(int V, int k) {
        if (V*k % 2 != 0) throw new IllegalArgumentException("Number of vertices * k must be even");
        Graph G = new Graph(V);

        int[] vertices = new int[V*k];
        for (int v = 0; v < V; v++) {
            for (int j = 0; j < k; j++) {
                vertices[v + V*j] = v;
            }
        }

        StdRandom.shuffle(vertices);
        for (int i = 0; i < V*k/2; i++) {
            G.addEdge(vertices[2*i], vertices[2*i + 1]);
        }
        return G;
    }

    public static Graph tree(int V) {
        Graph G = new Graph(V);

        if (V == 1) return G;

        int[] prufer = new int[V-2];
        for (int i = 0; i < V-2; i++)
            prufer[i] = StdRandom.uniformInt(V);

        int[] degree = new int[V];
        for (int v = 0; v < V; v++)
            degree[v] = 1;
        for (int i = 0; i < V-2; i++)
            degree[prufer[i]]++;

        MinPQ<Integer> pq = new MinPQ<Integer>();
        for (int v = 0; v < V; v++)
            if (degree[v] == 1) pq.insert(v);

        for (int i = 0; i < V-2; i++) {
            int v = pq.delMin();
            G.addEdge(v, prufer[i]);
            degree[v]--;
            degree[prufer[i]]--;
            if (degree[prufer[i]] == 1) pq.insert(prufer[i]);
        }
        G.addEdge(pq.delMin(), pq.delMin());
        return G;
    }

    // =================== MAIN ALTERADO =========================

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Digite o número de vértices: ");
        int V = sc.nextInt();
        System.out.print("Digite o número de arestas (se aplicável): ");
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

        System.out.print("Digite o caminho da pasta para salvar o arquivo: ");
        String pasta = sc.next();

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
                System.out.println("Opção inválida!");
                System.exit(0);
        }

        String caminhoArquivo = pasta + "/../Bases/Grafo.txt";
        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            writer.write(G.V() + " " + G.E() + "\n");
            for (int v = 0; v < G.V(); v++) {
                for (int w : G.adj(v)) {
                    if (v < w) {
                        writer.write(v + " " + w + "\n");
                    }
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