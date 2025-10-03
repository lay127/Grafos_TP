import java.util.*;

public class Fleury {

    // Remove a aresta u-v do grafo
    static void removeEdge(List<Integer>[] adj, int u, int v) {
        adj[u].remove(Integer.valueOf(v));
        adj[v].remove(Integer.valueOf(u));
    }

    // DFS para contar vértices alcançáveis
    static int dfsCount(int v, List<Integer>[] adj, boolean[] visited) {
        visited[v] = true;
        int count = 1;
        for (int neighbor : adj[v]) {
            if (!visited[neighbor]) {
                count += dfsCount(neighbor, adj, visited);
            }
        }
        return count;
    }

    // Verifica se a aresta u-v é válida para o próximo passo
    static boolean isValidNextEdge(int u, int v, List<Integer>[] adj, int totalV) {
        if (adj[u].size() == 1) return true;

        boolean[] visited = new boolean[totalV];
        int count1 = dfsCount(u, adj, visited);

        removeEdge(adj, u, v);

        Arrays.fill(visited, false);
        int count2 = dfsCount(u, adj, visited);

        adj[u].add(v);
        adj[v].add(u);

        return count1 == count2;
    }

    // Função recursiva que constrói o caminho/circuito Euleriano
    static void getEulerUtil(int u, List<Integer>[] adj, List<int[]> edges, int totalV) {
        for (int i = 0; i < adj[u].size(); i++) {
            int next = adj[u].get(i);
            if (isValidNextEdge(u, next, adj, totalV)) {
                edges.add(new int[]{u, next});
                removeEdge(adj, u, next);
                getEulerUtil(next, adj, edges, totalV);
                break;
            }
        }
    }

    // Função principal que retorna o caminho/circuito Euleriano
    static List<int[]> getEulerTour(int v, List<Integer>[] adj) {
        int start = 0;
        for (int i = 0; i < v; i++) {
            if (adj[i].size() % 2 != 0) {
                start = i;
                break;
            }
        }

        List<int[]> edges = new ArrayList<>();
        getEulerUtil(start, adj, edges, v);
        return edges;
    }

    // public static void main(String[] args) {
    //     int v = 4;
    //     List<Integer>[] adj = new ArrayList[v];
    //     for (int i = 0; i < v; i++) adj[i] = new ArrayList<>();

    //     // Grafo exemplo
    //     adj[0].add(1); adj[0].add(2);
    //     adj[1].add(0); adj[1].add(2);
    //     adj[2].add(0); adj[2].add(1); adj[2].add(3);
    //     adj[3].add(2);

    //     List<int[]> res = getEulerTour(v, adj);

    //     for (int i = 0; i < res.size(); i++) {
    //         System.out.print(res.get(i)[0] + "-" + res.get(i)[1]);
    //         if (i != res.size() - 1) System.out.print(", ");
    //     }
    // }
}

// Fonte: https://www.geeksforgeeks.org/dsa/fleurys-algorithm-for-printing-eulerian-path/