package com.example.sourcecodeview;

import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;
import java.util.Objects;


/**
 * Create by chenlei on 2019/12/21
 *
 * 使用Android无障碍功能，完成模拟点击。
 * 自动发送消息。
 * 使用时在无障碍界面开启该应用的服务。然后打开soul聊天界面即可
 */
public class SimulatedClickService extends AccessibilityService {
    private final static String TAG = "SimulatedClickService";
    private long count = 0L;

    //接收到系统发送AccessibilityEvent时的回调
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.e(TAG, "onAccessibilityEvent event: " + event);

        int eventType = event.getEventType();
        switch (eventType) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                //界面点击
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                //界面文字改动
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                if (event.getSource() != null) {
                    //遍历节点找到文本框，并输入消息
                    AccessibilityNodeInfo nodeInfo = event.getSource();
                    if (nodeInfo != null) {
                        recycleFindEditTexts(nodeInfo);
                    }
                    //找到发送按钮发送消息
                    List<AccessibilityNodeInfo> list = Objects.requireNonNull(event.getSource()).findAccessibilityNodeInfosByText("发送");
                    if (null != list) {
                        for (AccessibilityNodeInfo info : list) {
                            if (info.getText().toString().equals("发送")) {
                                // 找到你的节点以后就直接点击他就行了
                                info.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                                info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            }
                        }
                    }
                }
                break;
        }

    }

    /**
     * 遍历node节点找到文本框，并输入文字
     * @param node
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void recycleFindEditTexts(AccessibilityNodeInfo node) {
        //edittext下面必定没有子元素，所以放在此时判断
        if (node.getChildCount() == 0) {
            if ("android.widget.EditText".contentEquals(node.getClassName())) {
                count ++;
                String inputStr = count + "-" + getContent();
                Log.e(TAG, inputStr);
                node.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                Bundle arguments = new Bundle();
                arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, inputStr.trim());
                node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
            }
        } else {
            for (int i = 0; i < node.getChildCount(); i++) {
                if (node.getChild(i) != null) {
                    recycleFindEditTexts(node.getChild(i));
                }
            }
        }
    }

    //服务中断时的回调
    @Override
    public void onInterrupt() {
        Log.e(TAG, "onInterrupt");
    }

    /**
     * 得到随机文字
     * @return
     */
    private String getContent() {
        int index = 0;

        String[] contentArray = content.split("-");

        index = (int) (Math.random() * contentArray.length);
        if (index < 0) {
            index = 0;
        }
        if (index > contentArray.length) {
            index = contentArray.length;
        }

        return contentArray[index];
    }

    @Override
    public void onCreate() {
        super.onCreate();


    }

    private String content = "我喜欢的样子，你都有。-“我愿意”，说时来不及思索，但思索之后还是这样说\n" +
            "-我的故事，都是关于你啊！\n" +
            "-今夜我不关心人类，我只想你。\n" +
            "-从爱上你的那天起，甜蜜得很轻易\n" +
            "-你笑起来真像好天气。\n" +
            "-初次见面的时候，没想到会这样爱你。\n" +
            "-我转头，看见你走来，在阳光里，于是笑容从我心里溢出。\n" +
            "-听闻小姐治家有方，鄙人余生愿闻其详。\n" +
            "-你来的话，日子会甜一点。\n" +
            "-我的勇气和你的勇气加起来，对付这个世界总够了吧?去向世界发出我们的声音，我一个人是不敢的，有了你，我就敢 。\n" +
            "-在有你的选择里，我都选择你。\n" +
            "-你是我温暖的手套，冰冷的啤酒，带着阳光味道的衬衫，日复一日的梦想。\n" +
            "-在所有物是人非的风景里，我最喜欢你。\n" +
            "-夏天总想你。夏天不听话。\n" +
            "-你对我说的每一句话，都是沙漠里下过的雨。\n" +
            "-春日宴，绿酒一杯歌一遍。再拜陈三愿：一愿郎君千岁，二愿妾身常健，三愿如同梁上燕，岁岁长相见。\n" +
            "-你是年少的欢喜 这句话反过来也是你\n" +
            "-你是我这一生等了半世未拆的礼物。\n" +
            "-因为你太美好了，我等你等了这么久，才能跟你在一起，我害怕得不得了，生怕自己搞砸了。\n" +
            "-佛渡众生。你渡我。\n" +
            "-这个世界一点都不温柔，还好有你。\n" +
            "-我看过很多书，但都没有你好看。\n" +
            "-我想念你身体里波光万倾的海。\n" +
            "-要带着温度见你，哪怕是在梦里。\n" +
            "-在我贫瘠的土地上，你是最后的玫瑰。\n" +
            "-与君初相识，犹如故人归。天涯明月新，朝暮最相思。\n" +
            "-最近睡得很坏，最好你搬过来。\n" +
            "-我想要在茅亭里看雨，假山边看蚂蚁，看蝴蝶恋爱，看蜘蛛结网，看山，看船，看云，看瀑布，看宋清如甜甜地睡觉。\n" +
            "-想来想去，还是想你。\n" +
            "-你陪着我的时候，我从未羡慕过任何人。\n" +
            "-写了五行关于火的诗，两行烧茶，两行留到冬天取暖，剩下的一行，留给你在停电的晚上读我。\n" +
            "-我像铁一样拒绝不了你这般的磁石。\n" +
            "-你这种人，我除了恋爱跟你也没什么好谈的。\n" +
            "-其实我并不习惯晚睡，只是睡前想着你，然后想了很久。\n" +
            "-我其实只想和你在一起一次。哪怕只有61秒，25小时，13个月。\n" +
            "-“来者何人？”“你的人。”\n" +
            "-你是非常可爱的人，真应该遇到最好的人，我也真希望我就是。\n" +
            "-“我有个好小好小的梦想，才四个字。”，“嗯，是什么？”，“沿途有你。”\n" +
            "-我曾踏月而来，只因你在山中。\n" +
            "-我想作诗，写雨，写夜的相思，写你，写不出。\n" +
            "-皎皎白驹，在彼空谷。生刍一束，其人如玉。毋金玉尔音，而有遐心。\n" +
            "-南有乔木，不可休思，汉有游女，不可求思。\n" +
            "-明明早已百无禁忌，偏偏你是一百零一。\n" +
            "-绸缪束薪，三星在天。今夕何夕，见此良人。子兮子兮，如此良人何。\n" +
            "-偷偷在草稿纸上写你名字的人是我，下雪时偷偷在雪地里写你名字的是我，对反光镜哈气写你名字的是我，为了和你偶遇不惜绕路的是我，想为你瘦下来的是我，可是不知道的是你。\n" +
            "-我从不喜欢迁就，却用最干净的真心为你妥协了很久。\n" +
            "-未来我的生活只有简单十二个字“睡前吻你，半夜抱你，醒来有你”。\n" +
            "-我渴望与你打架，也渴望抱抱你。\n" +
            "-喜欢就去表白，大不了连朋友都做不成，做朋友又有什么用，我又不缺朋友，我缺你。\n" +
            "-醒来觉得甚是爱你。\n" +
            "-“春天的原野里，你一个人正走着，对面走来一只可爱的小熊，浑身的毛活像天鹅绒，眼睛圆鼓鼓的。它这么对你说到：‘你好，小姐，和我一块打滚玩好么？’接着，你就和小熊抱在一起，顺着长满三叶草的山坡咕噜咕噜滚下去，整整玩了一大天。你说棒不棒？” \n" +
            "-“太棒了。” \n" +
            "-“我就这么喜欢你。”\n" +
            "-我只有两个心愿：你在身边，在你身边。\n" +
            "-海底月是天上月，眼前人是心上人。\n" +
            "-你的眉目笑语使我病了一场，热势退尽，还我寂寞的健康。\n" +
            "-清晨的微笑给你，深夜的晚安给你，情话给你，钥匙给你，一腔孤勇和余生几十年，全都给你。\n" +
            "-跟我走吧，我真的想要一个家了。\n" +
            "-理想归理想，现实归现实，你归我！\n" +
            "-你知道吗，我不想做你的路人，我想做你余生故事里的人。\n" +
            "-自从遇见你，人生苦短，甜长。\n" +
            "-我的每一支笔都知道你的名字。\n" +
            "-我爱宇宙，爱星星尘埃，爱孤单际遇。我爱山河湖海，爱叽喳鸟鸣，爱茵茵绿芽。我爱四季更迭，爱化冰后的叮咚小溪，爱一树簌簌下落的飘雪，然后停在你这里。\n" +
            "-假如你愿意，就去恋爱吧，爱我。\n" +
            "-我发觉我是一个坏小子，你爸爸说的一点也不错。可是我现在不坏了，我有了良心。我的良心就是你。\n" +
            "-我羡慕那些和你在同一座城市的人，可以和你擦肩而过，乘坐同一辆地铁，走同一条路，看同一处风景，他们甚至还可能在汹涌的人潮中不小心踩了你一脚说对不起，再听你温柔道声没关系，他们那么幸运，而我只能从心里对你说：我想你。\n" +
            "-跟你在一起的时光都很耀眼，因为天气好，因为天气不好，因为天气刚刚好，每一天，都很美好。\n" +
            "-月亮照回湖心，野鹤奔向闲云，我步入你。\n" +
            "-你要是愿意，我就永远爱你 。你要是不愿意，我就永远相思。\n" +
            "-我遇见你，我记得你，这座城市天生就适合恋爱，你天生就适合我的灵魂。\n" +
            "-你辛苦归辛苦，什么时候有空嫁给我。\n" +
            "-一个人不孤单，想一个人才孤单。\n" +
            "-任她们多漂亮，未及你矜贵。\n" +
            "-纵然世上有三千个你，每个我都要娶\n" +
            "-我喜欢你总是藏不住，捂住了嘴巴会从眼睛里跑出来。\n" +
            "-过去有人曾对我说，“一个人爱上小溪，是因为没有见过大海。” 而如今我终于可以说，“我已见过银河，但我仍只爱你这一颗星。”\n" +
            "-我嫉妒你身边每一个无关紧要的人。他们就那样轻而易举，见到我朝思暮想的你。\n" +
            "-为你，千千万万遍。\n" +
            "-滔滔不绝很容易，可我只想和你在一个慢下来的世界里交谈。\n" +
            "-我把我整个灵魂都给你，连同它的怪癖，耍小脾气，忽明忽暗，一千八百种坏毛病。它真讨厌，只有一点好，爱你。\n" +
            "-你的过去我来不及参与，你的未来我奉陪到底。\n" +
            "-我觉得你是精灵。谵语及诗句，最后我都会念给你听。\n" +
            "-我等你扑来\n" +
            "-像莽撞的幼兽猛然跃起\n" +
            "-一路撞碎了几迎春风\n" +
            "-乌糟糟的衣服襟子里卷带了马兰头的新鲜叶子。\n" +
            "-若逢新雪初霁，满月当空，下面平铺着皓影，上面流转着亮银，而你带笑地向我走来 月色与雪色之间，你是第三种绝色。\n" +
            "-你说出来 就存在,你造出来 就崇拜,你说存在 就存在,你叫我爱 我就爱\n" +
            "-我在世界上有三种爱的东西,太阳 月亮 和你,太阳是为了早晨,月亮是为了夜晚,而你 是为了永远\n" +
            "-我想和你过漫无目地的日子，把世人的烦恼当作久远而荒唐的故事\n" +
            "-原谅我和你找不到那么多的话题，背后却满嘴都是你。\n" +
            "-有一个大晴天我睡了午觉起来，看到阳台上晒着你的衬衫和我的裙子，他们投射到地板上的影子，让我联想到了一生一世之类的词语。\n" +
            "-你眼中有春与秋，胜过我见过爱过的一切山川与河流。\n" +
            "-就让我笨拙地喜欢你，从须臾到不朽，从一叶到知秋，然后，骑一匹白马，到人海里安家。\n" +
            "-原谅我太贪心，陪了你情窦初开，还想陪你两鬓斑白。\n" +
            "-不知为何，明明想和你说话。却骗你说，风雨正好，该去写点诗句。\n" +
            "-我见过千万人，像你的发，像你的眼，却都不是你的脸。\n" +
            "-我与命运做的数十笔交易中，遇见你这单，最划算。\n" +
            "-如果天天都能看到你，已经是今世最好的福气了。";
}
