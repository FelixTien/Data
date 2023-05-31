//
//  DetailView.swift
//  FetchTest
//
//  Created by 田贊拓 on 2023/5/31.
//

import SwiftUI

struct DetailView: View {
    @EnvironmentObject var fetch: FetchModel
    var body: some View {
        ScrollView{
            VStack{
                VStack(alignment: .center){
                    Text("Meal Name :")
                        .font(.title)
                        .bold()
                    Text(fetch.detail[fetch.idMeal].meals[0].strMeal)
                        .font(.title2)
                }
                .padding()
                
                AsyncImage(url: URL(string: fetch.detail[fetch.idMeal].meals[0].strMealThumb)) { image in
                    image
                        .resizable()
                        .scaledToFill()
                        .cornerRadius(10)
                        .padding()
                        .shadow(radius: 15)
                } placeholder: {
                    ProgressView()
                }
                
                VStack{
                    Text("Instructions :")
                        .font(.title)
                        .bold()
                    Text(fetch.detail[fetch.idMeal].meals[0].strInstructions)
                        .font(.title2)
                }
                .padding()
                
                HStack{
                    VStack(alignment: .leading){
                        Text("Instructions : ")
                            .font(.title)
                            .bold()
                        DataView(
                            data1: fetch.detail[fetch.idMeal].meals[0].strIngredient1,
                            data2: fetch.detail[fetch.idMeal].meals[0].strIngredient2,
                            data3: fetch.detail[fetch.idMeal].meals[0].strIngredient3,
                            data4: fetch.detail[fetch.idMeal].meals[0].strIngredient4,
                            data5: fetch.detail[fetch.idMeal].meals[0].strIngredient5)
                        DataView(
                            data1: fetch.detail[fetch.idMeal].meals[0].strIngredient6,
                            data2: fetch.detail[fetch.idMeal].meals[0].strIngredient7,
                            data3: fetch.detail[fetch.idMeal].meals[0].strIngredient8,
                            data4: fetch.detail[fetch.idMeal].meals[0].strIngredient9,
                            data5: fetch.detail[fetch.idMeal].meals[0].strIngredient10)
                        DataView(
                            data1: fetch.detail[fetch.idMeal].meals[0].strIngredient11,
                            data2: fetch.detail[fetch.idMeal].meals[0].strIngredient12,
                            data3: fetch.detail[fetch.idMeal].meals[0].strIngredient13,
                            data4: fetch.detail[fetch.idMeal].meals[0].strIngredient14,
                            data5: fetch.detail[fetch.idMeal].meals[0].strIngredient15)
                        DataView(
                            data1: fetch.detail[fetch.idMeal].meals[0].strIngredient16,
                            data2: fetch.detail[fetch.idMeal].meals[0].strIngredient17,
                            data3: fetch.detail[fetch.idMeal].meals[0].strIngredient18,
                            data4: fetch.detail[fetch.idMeal].meals[0].strIngredient19,
                            data5: fetch.detail[fetch.idMeal].meals[0].strIngredient20)
                    }
                    Spacer()
                }
                .padding()
                
                HStack{
                    VStack(alignment: .leading){
                        Text("Measurements : ")
                            .font(.title)
                            .bold()
                        DataView(
                            data1: fetch.detail[fetch.idMeal].meals[0].strMeasure1,
                            data2: fetch.detail[fetch.idMeal].meals[0].strMeasure2,
                            data3: fetch.detail[fetch.idMeal].meals[0].strMeasure3,
                            data4: fetch.detail[fetch.idMeal].meals[0].strMeasure4,
                            data5: fetch.detail[fetch.idMeal].meals[0].strMeasure5)
                        DataView(
                            data1: fetch.detail[fetch.idMeal].meals[0].strMeasure6,
                            data2: fetch.detail[fetch.idMeal].meals[0].strMeasure7,
                            data3: fetch.detail[fetch.idMeal].meals[0].strMeasure8,
                            data4: fetch.detail[fetch.idMeal].meals[0].strMeasure9,
                            data5: fetch.detail[fetch.idMeal].meals[0].strMeasure10)
                        DataView(
                            data1: fetch.detail[fetch.idMeal].meals[0].strMeasure11,
                            data2: fetch.detail[fetch.idMeal].meals[0].strMeasure12,
                            data3: fetch.detail[fetch.idMeal].meals[0].strMeasure13,
                            data4: fetch.detail[fetch.idMeal].meals[0].strMeasure14,
                            data5: fetch.detail[fetch.idMeal].meals[0].strMeasure15)
                        DataView(
                            data1: fetch.detail[fetch.idMeal].meals[0].strMeasure16,
                            data2: fetch.detail[fetch.idMeal].meals[0].strMeasure17,
                            data3: fetch.detail[fetch.idMeal].meals[0].strMeasure18,
                            data4: fetch.detail[fetch.idMeal].meals[0].strMeasure19,
                            data5: fetch.detail[fetch.idMeal].meals[0].strMeasure20)
                    }
                    Spacer()
                }
                .padding()
            }
        }
    }
}

struct DetailView_Previews: PreviewProvider {
    static var previews: some View {
        DetailView()
            .environmentObject(FetchModel())
    }
}
