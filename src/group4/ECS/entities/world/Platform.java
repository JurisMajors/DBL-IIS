package group4.ECS.entities.world;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.DimensionComponent;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.PlatformComponent;
import group4.ECS.components.PositionComponent;
import group4.ECS.etc.TheEngine;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.maths.Vector3f;

import java.util.ArrayList;

public class Platform extends Entity {

    /**
     * Creates a static platform
     *
     * @param position  left-bottom-back corner of the cuboid representing the platform
     * @param dimension such that the right-top-front corner of the cuboid representing the platform is position+dimension
     * @param shader    shader for this platform
     * @param texture   texture for this platform
     */
    public Platform(Vector3f position, Vector3f dimension, Shader shader, Texture texture) {
        this.add(new PositionComponent(position));
        this.add(new DimensionComponent(dimension));
        this.add(new PlatformComponent());

        // create the graphics component with a vertex array repeating the texture over this platform
        GraphicsComponent graphicsComponent = createGraphicsComponent(position, dimension, texture, shader);
        this.add(graphicsComponent);
    }

    private GraphicsComponent createGraphicsComponent(Vector3f position, Vector3f dimension, Texture texture, Shader shader) {
        int texWidth = texture.getWidth();
        int texHeight = texture.getHeight();

        // boundaries for the platform on the top and right (local)
        float topY = position.add(dimension).y;
        float rightX = position.add(dimension).x;

        // temporary arraylists to store the vertex cooridnates, texture coordinates, indices
        ArrayList<Vector3f> vertexArray = new ArrayList<>();
        ArrayList<Vector3f> tcArray = new ArrayList<>();
        ArrayList<Byte> indices = new ArrayList<>();

        // keep track of at which index we are to make sure that the indices align properly
        byte startI = 0;
        // loop over all 'texture sized' blocks in this platform
        for (int x = 0; x <= rightX; x += texWidth) {
            for (int y = 0; y <= topY; y += texHeight) {
                // find the end x and y for the current texture block
                float endY = Math.min(topY, y + texHeight);
                float endX = Math.min(rightX, x + texWidth);

                // add a texture block with bottomleft(x,y) and topright(x+endX, y+endY)
                // bottomleft
                Vector3f blVertex = new Vector3f(x, y, 0.0f);
                Vector3f blTc = new Vector3f(0, 0, 0);
                // bottomright
                Vector3f brVertex = new Vector3f(x + endX, y, 0.0f);
                Vector3f brTc = new Vector3f((endX - x) / texWidth, 0, 0);
                // topright
                Vector3f trVertex = new Vector3f(x + endX, y + endY, 0.0f);
                Vector3f trTc = new Vector3f((endX - x) / texWidth, (endY - y) / texHeight, 0);
                // topleft
                Vector3f tlVertex = new Vector3f(x, y + endY, 0.0f);
                Vector3f tlTc = new Vector3f(x, (endY - y) / texHeight, 0);

                // add the vertices and texture coordinates
                vertexArray.add(blVertex);
                vertexArray.add(brVertex);
                vertexArray.add(trVertex);
                vertexArray.add(tlVertex);
                tcArray.add(blTc);
                tcArray.add(brTc);
                tcArray.add(trTc);
                tcArray.add(tlTc);

                // add the correct indices to make a square out of two triangles
                // first triangle
                indices.add(startI);
                indices.add((byte) (startI + 1));
                indices.add((byte) (startI + 2));
                // second triangle
                indices.add((byte) (startI + 2));
                indices.add((byte) (startI + 3));
                indices.add(startI);

                // we added 4 vertices so the index needs to be updated with +4
                startI += 4;
            }
        }

        // turn the arraylists into arrays for the VertexArray object
        float[] va = createVertexArray(vertexArray);
        float[] tc = createTextureArray(tcArray);
        byte[] ic = createIndicesArray(indices);

        return new GraphicsComponent(shader, texture, va, ic, tc);
    }


    /**
     * Creates float array from a vector3f arraylist for the vertexArray class.
     * For each vector in the list it adds x, y, z separately to the float array.
     *
     * @param al arraylist with vector3f
     * @return float array with (local/model) vertex coordinates
     */
    private float[] createVertexArray(ArrayList<Vector3f> al) {
        float[] result = new float[al.size() * 3];
        // add the x y z as a separate element
        for (int i = 0; i < al.size(); i++) {
            result[3 * i + 0] = al.get(i).x;
            result[3 * i + 1] = al.get(i).y;
            result[3 * i + 2] = al.get(i).z;
        }
        return result;
    }

    /**
     * Creates float array from a vector3f arraylist for texture coordinates.
     * For each vector in the list it adds the x, y separately to the float array.
     *
     * @param al
     * @return float array with texture coordinates
     */
    private float[] createTextureArray(ArrayList<Vector3f> al) {
        float[] result = new float[al.size() * 3];
        // add the x y z as a separate element
        for (int i = 0; i < al.size(); i++) {
            result[2 * i + 0] = al.get(i).x;
            result[2 * i + 1] = al.get(i).y;
        }
        return result;
    }

    private byte[] createIndicesArray(ArrayList<Byte> al) {
        byte[] result = new byte[al.size()];
        for (int i = 0; i < al.size(); i++) {
            result[i] = al.get(i);
        }
        return result;
    }
}
