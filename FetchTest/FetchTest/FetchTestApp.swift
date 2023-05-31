//
//  FetchTestApp.swift
//  FetchTest
//
//  Created by 田贊拓 on 2023/5/30.
//

import SwiftUI

@main
struct FetchTestApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
                .environmentObject(FetchModel())
        }
    }
}
