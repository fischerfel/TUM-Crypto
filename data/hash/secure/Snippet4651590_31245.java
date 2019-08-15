import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/* VPW Template */

public class Main
{

    /**
     * @param args
     */
    public static void main(String[] args) throws IOException
    {
        new Main().start();
    }

public void start() throws IOException
{

    /* Read the stuff */
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String[] input = new String[Integer.parseInt(br.readLine())];
    for (int i = 0; i < input.length; ++i)
    {
        input[i] = br.readLine();
    }
    /* Process each line */
    for (int i = 0; i < input.length; ++i)
    {
        processLine(input[i]);
    }
}

public void processLine(String line)
{
    int n = Integer.parseInt(line);
    System.out.println(countPolyminos(n));
}

private int countPolyminos(int n)
{
    hashes.clear();
    count = 0;
    boolean[][] matrix = new boolean[n][n];
    matrix[n / 2][n / 2] = true;
    createPolyominos(matrix, n - 1);
    return count;
}

private List<BigInteger> hashes = new ArrayList<BigInteger>();
private int count;

private void createPolyominos(boolean[][] matrix, int n)
{
    if (n == 0)
    {
        boolean[][] cropped = cropMatrix(matrix);
        BigInteger hash = hashMatrixOrientationIndependent(cropped);
        if (!hashes.contains(hash))
        {
            // System.out.println(count + " Found!");
            // printMatrix(cropped);
            // System.out.println();
            count++;
            hashes.add(hash);
        }
        return;
    }
    for (int x = 0; x < matrix.length; ++x)
    {
        for (int y = 0; y < matrix[x].length; ++y)
        {
            if (matrix[x][y])
            {
                if (x > 0 && !matrix[x - 1][y])
                {
                    boolean[][] clone = copy(matrix);
                    clone[x - 1][y] = true;
                    createPolyominos(clone, n - 1);
                }
                if (x < matrix.length - 1 && !matrix[x + 1][y])
                {
                    boolean[][] clone = copy(matrix);
                    clone[x + 1][y] = true;
                    createPolyominos(clone, n - 1);
                }
                if (y > 0 && !matrix[x][y - 1])
                {
                    boolean[][] clone = copy(matrix);
                    clone[x][y - 1] = true;
                    createPolyominos(clone, n - 1);
                }
                if (y < matrix[x].length - 1 && !matrix[x][y + 1])
                {
                    boolean[][] clone = copy(matrix);
                    clone[x][y + 1] = true;
                    createPolyominos(clone, n - 1);
                }
            }
        }
    }
}

public boolean[][] copy(boolean[][] matrix)
{
    boolean[][] b = new boolean[matrix.length][matrix[0].length];
    for (int i = 0; i < matrix.length; ++i)
    {
        System.arraycopy(matrix[i], 0, b[i], 0, matrix[i].length);
    }
    return b;
}

public void printMatrix(boolean[][] matrix)
{
    for (int y = 0; y < matrix.length; ++y)
    {
        for (int x = 0; x < matrix[y].length; ++x)
        {
            System.out.print((matrix[y][x] ? 'X' : ' '));
        }
        System.out.println();
    }
}

public boolean[][] cropMatrix(boolean[][] matrix)
{
    int l = 0, t = 0, r = 0, b = 0;
    // Left
    left: for (int x = 0; x < matrix.length; ++x)
    {
        for (int y = 0; y < matrix[x].length; ++y)
        {
            if (matrix[x][y])
            {
                break left;
            }
        }
        l++;
    }
    // Right
    right: for (int x = matrix.length - 1; x >= 0; --x)
    {
        for (int y = 0; y < matrix[x].length; ++y)
        {
            if (matrix[x][y])
            {
                break right;
            }
        }
        r++;
    }
    // Top
    top: for (int y = 0; y < matrix[0].length; ++y)
    {
        for (int x = 0; x < matrix.length; ++x)
        {
            if (matrix[x][y])
            {
                break top;
            }
        }
        t++;
    }
    // Bottom
    bottom: for (int y = matrix[0].length - 1; y >= 0; --y)
    {
        for (int x = 0; x < matrix.length; ++x)
        {
            if (matrix[x][y])
            {
                break bottom;
            }
        }
        b++;
    }

    // Perform the real crop
    boolean[][] cropped = new boolean[matrix.length - l - r][matrix[0].length - t - b];
    for (int x = l; x < matrix.length - r; ++x)
    {
        System.arraycopy(matrix[x], t, cropped[x - l], 0, matrix[x].length - t - b);
    }
    return cropped;
}

public BigInteger hashMatrix(boolean[][] matrix)
{
    try
    {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update((byte) matrix.length);
        md.update((byte) matrix[0].length);
        for (int x = 0; x < matrix.length; ++x)
        {
            for (int y = 0; y < matrix[x].length; ++y)
            {
                if (matrix[x][y])
                {
                    md.update((byte) x);
                } else
                {
                    md.update((byte) y);
                }
            }
        }
        return new BigInteger(1, md.digest());
    } catch (NoSuchAlgorithmException e)
    {
        System.exit(1);
        return null;
    }
}

public BigInteger hashMatrixOrientationIndependent(boolean[][] matrix)
{
    BigInteger hash = hashMatrix(matrix);
    for (int i = 0; i < 3; ++i)
    {
        matrix = rotateMatrixLeft(matrix);
        hash = hash.add(hashMatrix(matrix));
    }
    return hash;
}

public boolean[][] rotateMatrixRight(boolean[][] matrix)
{
    /* W and H are already swapped */
    int w = matrix.length;
    int h = matrix[0].length;
    boolean[][] ret = new boolean[h][w];
    for (int i = 0; i < h; ++i)
    {
        for (int j = 0; j < w; ++j)
        {
            ret[i][j] = matrix[w - j - 1][i];
        }
    }
    return ret;
}

public boolean[][] rotateMatrixLeft(boolean[][] matrix)
{
    /* W and H are already swapped */
    int w = matrix.length;
    int h = matrix[0].length;
    boolean[][] ret = new boolean[h][w];
    for (int i = 0; i < h; ++i)
    {
        for (int j = 0; j < w; ++j)
        {
            ret[i][j] = matrix[j][h - i - 1];
        }
    }
    return ret;
}
