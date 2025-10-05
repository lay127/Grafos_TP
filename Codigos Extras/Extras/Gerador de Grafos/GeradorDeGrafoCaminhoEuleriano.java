import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/******************************************************************************
 *  Gerador de grafos simples em Java.
 *  Funciona sem dependências externas.
 *  Permite gerar grafos com ou sem caminho Euleriano e salvar em arquivo TXT.
 ******************************************************************************/

public class GeradorDeGrafoCaminhoEuleriano {
    private static final class Edge {
        private int v, w;
        private Edge(int v, int w) {
            if (v < w) { this.v = v; this.w = w; }
            else { this.v = w; this.w = v; }
        }
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Edge)) return false;
            Edge e = (Edge) o;
            return this.v == e.v && this.w == e.w;
        }
        @Override
        public int hashCode() { return Objects.hash(v, w); }
    }

    static class Graph {
        private int V;
        private List<Integer>[] adj;

        @SuppressWarnings("unchecked")
        public Graph(int V) {
            this.V = V;
            adj = new ArrayList[V];
            for (int i = 0; i < V; i++) adj[i] = new ArrayList<>();
        }

        public void addEdge(int v, int w) {
            if (!adj[v].contains(w)) {
                adj[v].add(w);
                adj[w].add(v);
            }
        }

        public void removeEdge(int v, int w) {
            adj[v].remove((Integer) w);
            adj[w].remove((Integer) v);
        }

        public int V() { return V; }
        public int E() {
            int count = 0;
            for (List<Integer> l : adj) count += l.size();
            return count / 2;
        }
        public Iterable<Integer> adj(int v) { return adj[v]; }
    }

    private static Random rand = new Random();

    // =================== MÉTODOS DE GERAÇÃO =========================
    public static Graph eulerian(int V) {
        Graph G = new Graph(V);
        for (int i = 0; i < V; i++) {
            int j = (i + 1) % V;
            G.addEdge(i, j);
        }
        return G;
    }

    public static Graph nonEulerian(int V) {
        Graph G = eulerian(V);
        if (V >= 2) G.removeEdge(0, 1); // quebra Euleriano
        return G;
    }

    // =================== MAIN =========================
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Digite o numero de vertices: ");
        int V = sc.nextInt();

        System.out.println("Deseja um grafo com caminho Euleriano?");
        System.out.println("1 - Sim");
        System.out.println("2 - Nao");
        int opcao = sc.nextInt();

        Graph G = (opcao == 1) ? eulerian(V) : nonEulerian(V);

        String caminhoArquivo = "Bases/Grafo100000.txt";

        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            writer.write(G.V() + "  " + G.E() + "\n");

            for (int v = 0; v < G.V(); v++) {
                for (int w : G.adj(v)) {
                    if (v < w) writer.write(String.format("%7d %7d\n", v + 1, w + 1));
                }
            }

            System.out.println("Grafo salvo em: " + caminhoArquivo);
        } catch (IOException ex) {
            System.out.println("Erro ao salvar o arquivo: " + ex.getMessage());
        }

        sc.close();
    }
}