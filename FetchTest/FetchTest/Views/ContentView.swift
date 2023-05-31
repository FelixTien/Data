//
//  ContentView.swift
//  FetchTest
//
//  Created by 田贊拓 on 2023/5/30.
//

import SwiftUI

struct ContentView: View {
    @EnvironmentObject var fetch: FetchModel
    @Environment(\.colorScheme) var colorScheme
    @State var select = false
    var body: some View {
        NavigationView {
            List(fetch.detail, id: \.self){ d in
                Button {
                    self.select = true
                    fetch.idMeal = fetch.idMealFind(id: d.meals[0].idMeal)
                } label: {
                    HStack{
                        AsyncImage(url: URL(string: d.meals[0].strMealThumb)) { image in
                            image
                                .resizable()
                                .scaledToFill()
                                .frame(width: 70, height: 70)
                                .cornerRadius(5)
                        } placeholder: {
                            ProgressView()
                        }
                        Text(d.meals[0].strMeal)
                            .font(.headline)
                            .foregroundColor(colorScheme == .dark ? .white : .black)
                            .padding()
                    }
                }
                .sheet(isPresented: $select) {
                    DetailView()
                }
            }
            .navigationTitle("Fetch Test")
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
            .environmentObject(FetchModel())
    }
}
