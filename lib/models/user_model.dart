import 'package:cloud_firestore/cloud_firestore.dart';

class UserModel {
  final String email;
  final String nama;
  final String nohp;
  final String role;
  final String alamat;
  final String imageURL;


  UserModel(
      {
      required this.email,
      required this.nama,
      required this.nohp,
      required this.role,
      required this.alamat,
      required this.imageURL});

  Map<String, dynamic> toJson() => {
        "email": email,
        "nama": nama,
        "nohp": nohp,
        "role": role,
        "alamat": alamat,
        "imageURL": imageURL,
      };

  static UserModel? fromSnap(DocumentSnapshot snap) {
    var snapshot = snap.data() as Map<String, dynamic>;
    return UserModel(
      email: snapshot['email'],
      nama: snapshot['nama'],
      nohp: snapshot['nohp'],
      role: snapshot['role'],
      alamat: snapshot['alamat'],
      imageURL: snapshot['imageURL'],
    );
  }
}
