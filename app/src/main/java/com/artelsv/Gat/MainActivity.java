package com.artelsv.Gat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.artelsv.Gat.creatures.Hero;
import com.artelsv.Gat.creatures.effects.Effect;
import com.artelsv.Gat.items.InvenotoryAdapter;
import com.artelsv.Gat.items.Item;
import com.artelsv.Gat.items.ShopAdapter;

import java.util.ArrayList;
import java.util.Timer;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    final static boolean DEBUG = false;

    //Timer Vars
    private final int UPDATETIME = 1000; //в миллесекундах
    ProgressBar progressTimer;
    ProgressTimerTask progressTimerTask;
    Timer timer;
    //
    //Hero
    Hero hero;
    //
    //
    //Buttons and events shit
    TextView textView;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    //
    //Fragment TextViews
    TextView textCoin;
    TextView textPower;
    TextView textProtection;
    TextView textHp;
    TextView statsInfo;
    //
    //Inventory
    RecyclerView inventory_store;
    LinearLayoutManager gm;
    InvenotoryAdapter ia;
    //Shop
    RecyclerView shop_store;
    LinearLayoutManager gm_2;
    ShopAdapter ia_2;

    //all items and effects
    ArrayList<Item> items;
    ArrayList<Item> storeItems;
    ArrayList<Effect> effects;
    //
    //Fragments (это крч кастыль. Можно было умнее сделать. Но так проще)
    View inventoryFrag;
    View shopFrag;
        //Fragments views
        Integer selectedItem = -1;
        Integer shopSelectedItem = -1;

        ImageButton sellItem;

        TextView ifItemName;
        TextView ifItemDesc;
        TextView ifInvInfo;

        TextView shopTextName;
        TextView shopTextDesc;
        TextView shopTextInfo;
    //Quest shit
    QuestLoader questLoader;
    QuestStore questStore;
    Quest curQuest;

    GifImageView gifImageView;

    ScrollView scrollView;

    MediaPlayer mp;
    View viewMap;

    Integer chapter;
    int curChapterCount;
    final int chapterCount_1 = 28; //меняй эти числа, в зависимости от кол-ва квестов в главе.
    final int chapterCount_2 = 50;

    private int currentApiVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        //progressTimer.incrementProgressBy(25);
        startTimer();
    }

    private void startTimer(){

    }
    //init
    private void init(){
        initHideButtonsBar();
        initProgressTimer();
        initHero();
        initEffects();
        initItems();
        initFragmentTextViews();
        initInventoryStore();
        initShopStore();
        initFragments();
        initEvents();
        initQuestLoader();
        initFonts();
        initChapters();
        initMediaPlayer();

        updateStats();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    } // НЕТРОГАТЬ БЛЯТЬ 2

    private void initProgressTimer(){ //Ход времени в игре

        progressTimer = findViewById(R.id.progressBar);

//        progressTimerTask = new ProgressTimerTask(progressTimer);
//        timer = new Timer();
//        timer.scheduleAtFixedRate(progressTimerTask,0,UPDATETIME);
    }
    /*      Обновление прогрессбара
    output: FALSE - не происходит переход на новую главу
            TRUE  - переход на новую главу              */
    private boolean updateProgress(){

        if (progressTimer.getProgress() >= progressTimer.getMax()){
            return true;
        } else {
            progressTimer.incrementProgressBy(1);
        }
        return false;
    }

    private void initHero(){
        hero = new Hero("Adventure", "тест", 10, 100, 1, 1);
    }

    private void initEffects(){
        effects = new ArrayList<Effect>();
        Effect newEffect;
        effects.add(newEffect = new Effect("heal", 1, 1));
        //Effects init

    }

    private void initItems(){ // Загрузка итемов
        //init inv items
        items = new ArrayList<Item>();

        Item newItem;
        items.add(newItem = new Item("tew6", "t6es", 6, 0, effects.get(0), getDrawable(R.drawable.slot_head), false));
        items.add(newItem = new Item("tew6", "t6es", 6, 0, effects.get(0), getDrawable(R.drawable.slot_head), false));
        items.add(newItem = new Item("tew6", "t6es", 6, 0, effects.get(0), getDrawable(R.drawable.slot_head), false));
        items.add(newItem = new Item("tew6", "t6es", 6, 0, effects.get(0), getDrawable(R.drawable.slot_head),false));

        //init shop items
        storeItems = new ArrayList<>();
        storeItems.add(new Item("Money Potion", "Выпейте, и в кармане станет на 2 монеты больше", 5, 1, new Effect("moneyinc", 2, 2), getDrawable(R.drawable.slot), false));
    }

    private void initHideButtonsBar(){
        currentApiVersion = android.os.Build.VERSION.SDK_INT;
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        decorView.setSystemUiVisibility(flags);
                    }
                }
            });
        }
    } // НЕТРОГАТЬ

    private void initFragmentTextViews(){
        textCoin = findViewById(R.id.textCoin);
        textHp = findViewById(R.id.textHp);
        textPower = findViewById(R.id.textPower);

        //inventory fragment
        ifItemName = findViewById(R.id.textView3);
        ifItemDesc = findViewById(R.id.textView4);
        sellItem = findViewById(R.id.sellButton);
    }

    private void initInventoryStore(){
        inventory_store = findViewById(R.id.inventory_store);
        gm = new GridLayoutManager(this,2);
        inventory_store.setLayoutManager(gm);
        inventory_store.setHasFixedSize(true);
        ia = new InvenotoryAdapter(items);
        inventory_store.setAdapter(ia);

        ifInvInfo = findViewById(R.id.invItemInfo);
        statsInfo = findViewById(R.id.textStats);

        inventory_store.addOnItemTouchListener(new RecyclerTouchListener(this, inventory_store, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.e("test", String.valueOf(position));
                Log.e("test", items.get(position).toString());
                ifItemName.setText(items.get(position).getName());
                ifItemDesc.setText(items.get(position).getDesc());
                selectedItem = position;



            }

            @Override
            public void onLongClick(View view, int position) {
                if (ia.getItems().get(selectedItem).isEquip() == true){
                    shopTextName.setText("");
                    shopTextDesc.setText("Предмет одет!");
                } else {
                    ia.getItems().get(selectedItem).getEffect().EffectCast(hero);
                    updateStats();
                    ia.removeAt(selectedItem);
                    ifItemName.setText("");
                    ifItemDesc.setText("Предмет использован!");
                }
            }
        }));
    }

    private void initShopStore(){
        shop_store = findViewById(R.id.shop_store);
        gm_2 = new GridLayoutManager(this,2);
        shop_store.setLayoutManager(gm_2);
        shop_store.setHasFixedSize(true);
        ia_2 = new ShopAdapter(storeItems);
        shop_store.setAdapter(ia_2);

        shopTextDesc = findViewById(R.id.shop_tvItemDesc);
        shopTextName = findViewById(R.id.shop_tvItemName);
        shopTextInfo = findViewById(R.id.textView2);


        shop_store.addOnItemTouchListener(new RecyclerTouchListener(this, shop_store, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.e("test", String.valueOf(position));
                Log.e("test", storeItems.get(position).toString());
                shopTextName.setText(storeItems.get(position).getName());
                shopTextDesc.setText(storeItems.get(position).getDesc());
                shopSelectedItem = position;
                Log.e("test1", shopSelectedItem.toString() + ia_2.getItems().get(shopSelectedItem).getPrice().toString());



            }

            @Override
            public void onLongClick(View view, int position) {
                if ((shopSelectedItem >= 0) && (ia_2.getItems().get(shopSelectedItem).getPrice() <= hero.getMoney())){
                    int change = (hero.getMoney())-(ia_2.getItems().get(shopSelectedItem).getPrice());
                    hero.setMoney(change);

                    items.add(storeItems.get(shopSelectedItem));
                    ia_2.removeAt(shopSelectedItem);
                    updateInvAndStore();
//                    Effect effect = new Effect("dm", -(ia_2.getItems().get(shopSelectedItem).getPrice()),2);
//                    effect.EffectCast(hero);

                    shopTextName.setText("");
                    shopTextDesc.setText("Предмет куплен!");
                } else if (shopSelectedItem < 0) {
                    shopTextName.setText("");
                    shopTextDesc.setText("Прежде выберите предмет!");
                } else if (ia_2.getItems().get(shopSelectedItem).getPrice() > hero.getMoney()) {
                    shopTextName.setText("");
                    shopTextDesc.setText("У вас недостаточно денег!");
                }

                updateStats();
                shopSelectedItem = -1;
            }
        }));
    }

    private void initFragments(){
        inventoryFrag = findViewById(R.id.fragmentInventory);
        shopFrag = findViewById(R.id.fragmentShop);

        shopFrag.setVisibility(View.INVISIBLE);
    }

    private void initQuestLoader(){
        int[] drawables = {R.drawable.koster_main, R.drawable.pustok};
        gifImageView = findViewById(R.id.gifImageView);
        scrollView = findViewById(R.id.event_textScroll);

        questLoader = new QuestLoader(textView, button1, button2, button3, button4, scrollView);
        questStore = new QuestStore(drawables, hero);

        questLoader.loadQuest(questStore.getQuests().get(0));
        curQuest = questStore.getQuests().get(0);
    }

    private void initEvents(){
        textView = findViewById(R.id.textView);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
    }

    private void initChapters(){
        chapter = 1;
        curChapterCount = chapterCount_1;
        progressTimer.setMax(curChapterCount);
    }

    private void initFonts(){
        Typeface type = Typeface.createFromAsset(getAssets(),"19190.ttf");
        textCoin.setTypeface(type);
        textHp.setTypeface(type);
        textPower.setTypeface(type);
//        textProtection.setTypeface(type);
        textView.setTypeface(type);
        ifItemDesc.setTypeface(type);
        ifItemName.setTypeface(type);
        button1.setTypeface(type);
        button2.setTypeface(type);
        button3.setTypeface(type);
        button4.setTypeface(type);
        ifInvInfo.setTypeface(type);
        statsInfo.setTypeface(type);
        shopTextName.setTypeface(type);
        shopTextInfo.setTypeface(type);
        shopTextDesc.setTypeface(type);
    }

    private void initMediaPlayer(){
        viewMap = findViewById(R.id.view_map);
        mp = MediaPlayer.create(this, R.raw.music1);

        //mp.prepareAsync();

        mp.setLooping(true);
        mp.start();

//        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                mp.reset();
//                mp.start();
//            }
//        });
    }
    //
    //inGame
    private void updateStats(){
        textCoin.setText(hero.getMoney().toString());
        textPower.setText(hero.getPower().toString());
        textHp.setText(hero.getHp().toString());
    }
    //

    private void updateInvAndStore(){
        ia.setItems(items);
        ia_2.setItems(storeItems);
    }

    //click listener
    public void button1Click(View view) { // чекаем нажатие кнопок
        switch (view.getId()){

            /*     КНОПКИ ИВЕНТОВ И ОБРАБОТКА НАЖАТИЙ
            Переход на новые квесты и каст эффектов*/
            case R.id.button1: // 1 Button 1
                if (DEBUG) {
                    Log.e("test", hero.toString());
                    Log.e("test", items.get(0).toString());
                }

                updateProgress();
                if (progressTimer.getProgress() == curChapterCount){
                    textView.setText("");

                    progressTimer.setProgress(0);
                    curChapterCount = chapterCount_2;
                    progressTimer.setMax(curChapterCount);
                    viewMap.setBackground(getDrawable(R.drawable.map_glava2));
                }

                curQuest.getEffects()[0].EffectCast(hero);
                updateStats();
                gifImageView.setImageResource(curQuest.getDrawables()[0]);
                questLoader.loadQuest(questStore.getQuests().get(curQuest.getNextQuestID()[0]));
                curQuest = questStore.getQuests().get(curQuest.getNextQuestID()[0]);

                break;
            case R.id.button2: // 2 Button 2
                if (DEBUG) {
                    Effect effect = new Effect("test", 2, 2);
                    effect.EffectCast(hero);
                    updateStats();
                }

                updateProgress();
                if (progressTimer.getProgress() == curChapterCount){
                    textView.setText("");

                    progressTimer.setProgress(0);
                    curChapterCount = chapterCount_2;
                    progressTimer.setMax(curChapterCount);
                    viewMap.setBackground(getDrawable(R.drawable.map_glava2));
                }

                curQuest.getEffects()[1].EffectCast(hero);
                updateStats();
                gifImageView.setImageResource(curQuest.getDrawables()[1]);
                //gifImageView.setImageResource(R.drawable.test);
                questLoader.loadQuest(questStore.getQuests().get(curQuest.getNextQuestID()[1]));
                curQuest = questStore.getQuests().get(curQuest.getNextQuestID()[1]);

                break;
            case R.id.button3: // 3 Button 3
                if (DEBUG) {

                }

                updateProgress();
                if (progressTimer.getProgress() == curChapterCount){
                    textView.setText("");

                    progressTimer.setProgress(0);
                    curChapterCount = chapterCount_2;
                    progressTimer.setMax(curChapterCount);
                    viewMap.setBackground(getDrawable(R.drawable.map_glava2));
                }

                curQuest.getEffects()[2].EffectCast(hero);
                updateStats();
                gifImageView.setImageResource(curQuest.getDrawables()[2]);
                questLoader.loadQuest(questStore.getQuests().get(curQuest.getNextQuestID()[2]));
                curQuest = questStore.getQuests().get(curQuest.getNextQuestID()[2]);
                break;
            case R.id.button4: // 4 Button 4
                if (DEBUG) {

                }

                updateProgress();
                if (progressTimer.getProgress() == curChapterCount){
                    textView.setText("");

                    progressTimer.setProgress(0);
                    curChapterCount = chapterCount_2;
                    progressTimer.setMax(curChapterCount);
                    viewMap.setBackground(getDrawable(R.drawable.map_glava2));
                }

                curQuest.getEffects()[3].EffectCast(hero);
                updateStats();
                gifImageView.setImageResource(curQuest.getDrawables()[3]);
                questLoader.loadQuest(questStore.getQuests().get(curQuest.getNextQuestID()[3]));
                curQuest = questStore.getQuests().get(curQuest.getNextQuestID()[3]);
                break;
            //кнопки ивентов

            case R.id.shopButton:
                shopFrag.setVisibility(View.VISIBLE);
                inventoryFrag.setVisibility(View.INVISIBLE);
                break;
            case R.id.inventoryButton:
                shopFrag.setVisibility(View.INVISIBLE);
                inventoryFrag.setVisibility(View.VISIBLE);
                break;
            case R.id.sellButton:
                if (selectedItem >= 0) {
                    ia.removeAt(selectedItem);
                    ifItemName.setText("");
                    ifItemDesc.setText("Предмет выброшен!");
                } else {
                    ifItemName.setText("");
                    ifItemDesc.setText("Прежде выберите предмет!");
                }
                Log.e("test","sellButton");
                break;
            case R.id.useItemButton:
                //ia.selected_item = selectedItem;

                if (selectedItem >= 0){
                    if (ia.getItems().get(selectedItem).isEquip() == true){
                        shopTextName.setText("");
                        shopTextDesc.setText("Предмет одет!");
                    } else {
                        ia.getItems().get(selectedItem).getEffect().EffectCast(hero);
                        updateStats();
                        ia.removeAt(selectedItem);
                        shopTextName.setText("");
                        shopTextDesc.setText("Предмет использован!");
                    }
                }

                selectedItem = -1;
                Log.e("test", "test");
                break;
            case R.id.shop_btnBuyItem:

                if ((shopSelectedItem >= 0) && (ia_2.getItems().get(shopSelectedItem).getPrice() <= hero.getMoney())){
                    int change = (hero.getMoney())-(ia_2.getItems().get(shopSelectedItem).getPrice());
                    hero.setMoney(change);

                    items.add(storeItems.get(shopSelectedItem));
                    ia_2.removeAt(shopSelectedItem);
                    updateInvAndStore();
//                    Effect effect = new Effect("dm", -(ia_2.getItems().get(shopSelectedItem).getPrice()),2);
//                    effect.EffectCast(hero);

                    shopTextName.setText("");
                    shopTextDesc.setText("Предмет куплен!");
                } else if (shopSelectedItem < 0) {
                    shopTextName.setText("");
                    shopTextDesc.setText("Прежде выберите предмет!");
                } else if (ia_2.getItems().get(shopSelectedItem).getPrice() > hero.getMoney()) {
                    shopTextName.setText("");
                    shopTextDesc.setText("У вас недостаточно денег!");
                }

                updateStats();
                shopSelectedItem = -1;
                break;

        }

    }
}
