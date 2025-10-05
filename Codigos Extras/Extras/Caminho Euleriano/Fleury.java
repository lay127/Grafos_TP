import java.util.*;

public class Fleury {

    // Remove a aresta u-v do grafo
    static void removeEdge(List<Integer>[] adj, int u, int v) {
        adj[u].remove(Integer.valueOf(v));
        adj[v].remove(Integer.valueOf(u));
    }

    // DFS para contar vértices alcançáveis a partir de v
    static int dfsCount(int v, List<Integer>[] adj, boolean[] visited) {
        visited[v] = true;
        int count = 1; // Conta o próprio vértice
        for (int neighbor : adj[v]) {
            if (!visited[neighbor]) {
                count += dfsCount(neighbor, adj, visited);
            }
        }
        return count;
    }

    // Verifica se a aresta u-v pode ser removida sem desconectar o grafo
    static boolean isValidNextEdge(int u, int v, List<Integer>[] adj, int totalV) {
        // Se u tem apenas uma aresta restante, esta aresta deve ser usada
        if (adj[u].size() == 1) return true;

        boolean[] visited = new boolean[totalV];
        int count1 = dfsCount(u, adj, visited);

        removeEdge(adj, u, v); // Remove a aresta temporariamente

        Arrays.fill(visited, false);
        int count2 = dfsCount(u, adj, visited);

        // Recoloca a aresta de volta
        adj[u].add(v);
        adj[v].add(u);

        // A aresta é válida se sua remoção não desconectar o grafo
        return count1 == count2;
    }

    // Função recursiva que "percorre" o caminho Euleriano (não armazenamos, só verificamos)
    static boolean hasEulerPathUtil(int u, List<Integer>[] adj, int totalV) {
        for (int i = 0; i < adj[u].size(); i++) {
            int next = adj[u].get(i);
            if (isValidNextEdge(u, next, adj, totalV)) {
                removeEdge(adj, u, next); // Remove a aresta usada
                return hasEulerPathUtil(next, adj, totalV);
            }
        }
        return true;
    }

    // Verifica se existe caminho Euleriano
    static boolean hasEulerPath(List<Integer>[] adj) {
        int oddCount = 0;
        int start = 0;
        int v = adj.length;

        // Contar vértices de grau ímpar
        for (int i = 0; i < v; i++) {
            if (adj[i].size() % 2 != 0) {
                oddCount++;
                start = i; // Começa de um vértice ímpar, se houver
            }
        }

        // Condição: 0 ou 2 vértices ímpares
        if (oddCount != 0 && oddCount != 2) return false;

        // Chama a função que "tenta percorrer" o grafo
        return hasEulerPathUtil(start, adj, v);
    }

    public static void main(String[] args) {

        // Exemplo de uso

        int v = 4;

        // ⚠️ Para evitar o aviso de tipo: cast seguro
        @SuppressWarnings("unchecked")
        List<Integer>[] adj = (List<Integer>[]) new ArrayList[v];
        for (int i = 0; i < v; i++) adj[i] = new ArrayList<>();

        // Grafo de exemplo
        adj[0].add(1); adj[0].add(2);
        adj[1].add(0); adj[1].add(2);
        adj[2].add(0); adj[2].add(1); adj[2].add(3);
        adj[3].add(2);

        if (hasEulerPath(adj)) {
            System.out.println("Existe caminho Euleriano: SIM");
        } else {
            System.out.println("Existe caminho Euleriano: NÃO");
        }
    }
}

// Fonte: https://www.geeksforgeeks.org/dsa/fleurys-algorithm-for-printing-eulerian-path/ 