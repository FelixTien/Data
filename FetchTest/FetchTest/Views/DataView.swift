//
//  DataView.swift
//  FetchTest
//
//  Created by 田贊拓 on 2023/5/31.
//

import SwiftUI

struct DataView: View {
    var data1: String
    var data2: String
    var data3: String
    var data4: String
    var data5: String
    var body: some View {
        VStack(alignment: .leading){
            if(data1 != "" && data1 != " "){
                Text("◾ \(data1)")
                    .font(.title3)
            }
            if(data2 != "" && data2 != " "){
                Text("◾ \(data2)")
                    .font(.title3)
            }
            if(data3 != "" && data3 != " "){
                Text("◾ \(data3)")
                    .font(.title3)
            }
            if(data4 != "" && data4 != " "){
                Text("◾ \(data4)")
                    .font(.title3)
            }
            if(data5 != "" && data5 != " "){
                Text("◾ \(data5)")
                    .font(.title3)
            }
        }
    }
}

struct DataView_Previews: PreviewProvider {
    static var previews: some View {
        DataView(data1: "1.", data2: "2.", data3: "3.", data4: "4.", data5: "5.")
    }
}
