package com.myudog.myulib.api.core.bridge.voice;

import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import de.maxhenkel.voicechat.api.events.VoicechatServerStartedEvent;

import java.util.UUID;

/**
 * 負責與 Simple Voice Chat (SVC) 進行介接的插件核心。
 * 攔截玩家語音並將狀態或音訊數據導出，為後續 AI 代理人提供聽覺輸入。
 */
public class MyuVoicePlugin implements VoicechatPlugin {

    public static VoicechatApi voicechatApi;
    public static VoicechatServerApi serverApi;

    /**
     * @return 你的插件唯一識別碼 (Plugin ID)
     */
    @Override
    public String getPluginId() {
        return "myulib_voice_bridge";
    }

    /**
     * SVC 初始化時呼叫，取得核心 API 實例。
     */
    @Override
    public void initialize(VoicechatApi api) {
        voicechatApi = api;
        System.out.println("[MyuLib-Voice] Simple Voice Chat API 已成功橋接！");
    }

    /**
     * 註冊我們需要監聽的語音事件。
     */
    @Override
    public void registerEvents(EventRegistration registration) {
        // 伺服器端 Voice Chat 啟動事件
        registration.registerEvent(VoicechatServerStartedEvent.class, this::onServerStarted);

        // 🌟 核心：監聽玩家麥克風傳來的音訊封包
        registration.registerEvent(MicrophonePacketEvent.class, this::onMicrophonePacket);
    }

    private void onServerStarted(VoicechatServerStartedEvent event) {
        serverApi = event.getVoicechat();
        System.out.println("[MyuLib-Voice] 語音伺服器已啟動，準備接收玩家音訊串流。");
    }

    /**
     * 當有玩家按下麥克風說話時，會高頻率觸發此事件。
     */
    private void onMicrophonePacket(MicrophonePacketEvent event) {
        // 取得發話者的連線物件與 UUID
        if (event.getSenderConnection() == null) return;
        UUID speakerId = event.getSenderConnection().getPlayer().getUuid();

        // 取得原始的 Opus 編碼音訊數據
        byte[] opusData = event.getPacket().getOpusEncodedData();

        // TODO: 在這裡，我們未來可以將 opusData 透過 WebSocket 拋出
        // 目前先實作簡單的防刷屏 Log 或狀態更新，證明我們成功攔截到了訊號

        // 提示：為了避免 Log 爆炸，不要在這裡直接 println，因為每秒會觸發數十次
        // MyuWSServer.broadcastTalkingState(speakerId, true);
    }
}