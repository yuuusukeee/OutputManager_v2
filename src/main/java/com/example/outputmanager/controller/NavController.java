package com.example.outputmanager.controller;

import org.springframework.stereotype.Controller;

/**
 * ルーティング競合を避けるため、画面遷移用の @GetMapping は持たせない。
 * 一覧／カテゴリの遷移は OutputController に集約する。
 */
@Controller
public class NavController {
    // 意図的に空。ここに @GetMapping は置かないこと。
}