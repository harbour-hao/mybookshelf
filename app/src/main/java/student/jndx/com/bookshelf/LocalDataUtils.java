package student.jndx.com.bookshelf;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 本地存储，处理只存储在本地的数据
 */
public class LocalDataUtils {

    private static SharedPreferences local_storage = null;
    private static final String seq = ";";

    public static void init_local_storage(Context context) {
        if (local_storage == null)
            local_storage = context.getSharedPreferences("tag", Context.MODE_PRIVATE);
    }

    /**
     * 读取标签
     *
     * @return
     */
    public static List<String> read_all_tag() {
        String all_tag = local_storage.getString("all_tag", null);
        if (all_tag != null) {
            String[] tags = all_tag.split(seq);
            List<String> tags_list = (List<String>) Arrays.asList(tags);
            return tags_list;
        } else
            return null;

    }

    /**
     * 添加标签
     *
     * @param tag
     */
    public static boolean insert_tag(String tag) {
        if (is_contain_seq(tag))
            return false;
        List<String> tags_list = read_all_tag();
        if (tags_list != null && tags_list.contains(tag)) {
            return false;
        } else {
            StringBuilder tags_builder = new StringBuilder();
            if (tags_list != null) {
                for (int i = 0; i < tags_list.size(); i++) {
                    tags_builder.append(tags_list.get(i));
                    tags_builder.append(seq);
                }
            }
            tags_builder.append(tag);
            tags_builder.append(seq);
            local_storage.edit().putString("all_tag", tags_builder.toString()).commit();
            return true;
        }

    }

    /**
     * 测试新的标签是否包含有分隔符
     *
     * @param tag
     * @return
     */
    public static boolean is_contain_seq(String tag) {
        if (tag.contains(seq)) {
            return true;
        }
        return false;
    }

    /**
     * 修改标签
     *
     * @param old_tag
     * @param new_tag
     */
    public static boolean rename_tag(String old_tag, String new_tag) {
        if (is_contain_seq(new_tag))
            return false;
        List<String> tags_list = read_all_tag();
        ArrayList arrayList;
        if (tags_list instanceof ArrayList) {
            arrayList = (ArrayList) tags_list;
        } else {
            arrayList = new ArrayList();
            arrayList.addAll(tags_list);
        }
        tags_list = arrayList;
        if (tags_list != null && tags_list.contains(old_tag)) {
            int index = tags_list.indexOf(old_tag);
            tags_list.remove(index);
            tags_list.add(index, new_tag);
        }
        StringBuilder tags_builder = new StringBuilder();
        for (int i = 0; i < tags_list.size(); i++) {
            tags_builder.append(tags_list.get(i));
            tags_builder.append(seq);
        }
        local_storage.edit().putString("all_tag", tags_builder.toString()).commit();
        return true;
    }

    /**
     * 删除标签
     *
     * @param tag
     */
    public static void delete_tag(String tag) {
        List<String> tags_list = read_all_tag();
        ArrayList arrayList;
        if (tags_list instanceof ArrayList) {
            arrayList = (ArrayList) tags_list;
        } else {
            arrayList = new ArrayList();
            arrayList.addAll(tags_list);
        }
        tags_list = arrayList;
        if (tags_list != null && tags_list.contains(tag)) {
            int index = tags_list.indexOf(tag);
            tags_list.remove(index);
        }
        if (tags_list.size() == 0) {
            local_storage.edit().putString("all_tag", null).commit();
            return;
        }
        StringBuilder tags_builder = new StringBuilder();
        for (int i = 0; i < tags_list.size(); i++) {
            tags_builder.append(tags_list.get(i));
            tags_builder.append(seq);
        }
        local_storage.edit().putString("all_tag", tags_builder.toString()).commit();
    }

}
