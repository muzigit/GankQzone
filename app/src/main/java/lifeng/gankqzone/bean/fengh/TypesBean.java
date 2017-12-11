package lifeng.gankqzone.bean.fengh;

/**
 * Created by lifeng on 2017/11/17.
 *
 * @description
 */
public class TypesBean {

    /**
     * chType : video
     * id : 9
     * name : 娱乐
     * position : down
     */

    private String chType;
    private int id;
    private String name;
    private String position;

    public String getChType() {
        return chType;
    }

    public void setChType(String chType) {
        this.chType = chType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "TypesBean{" +
                "chType='" + chType + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", position='" + position + '\'' +
                '}';
    }
}
