import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../models/property.dart';
import '../providers/property_provider.dart';
import '../theme/app_theme.dart';

class AddPropertyScreen extends StatefulWidget {
  const AddPropertyScreen({super.key});

  @override
  State<AddPropertyScreen> createState() => _AddPropertyScreenState();
}

class _AddPropertyScreenState extends State<AddPropertyScreen> {
  final _formKey = GlobalKey<FormState>();

  String _title = '';
  double _price = 0;
  String _purpose = 'للبيع';
  String _type = 'شقة';
  String _city = 'الرياض';
  String _neighborhood = '';
  double _area = 0;
  int _bedrooms = 3;
  int _bathrooms = 2;
  String _description = '';

  final List<String> _cities = ['الرياض', 'جدة', 'الدمام', 'الخبر', 'مكة المكرمة'];
  final List<String> _types = ['فيلا', 'شقة', 'دور', 'أرض'];
  final List<String> _purposes = ['للبيع', 'للإيجار'];

  void _submit() {
    if (_formKey.currentState!.validate()) {
      _formKey.currentState!.save();

      final newProperty = Property(
        id: DateTime.now().millisecondsSinceEpoch,
        title: _title,
        price: _price,
        city: _city,
        neighborhood: _neighborhood,
        purpose: _purpose,
        type: _type,
        area: _area,
        bedrooms: _bedrooms,
        bathrooms: _bathrooms,
        description: _description.isEmpty
            ? "عقار مميز في حي $_neighborhood بمدينة $_city، مجهز بكافة الخدمات الأساسية ومتاح للإفراغ المباشر."
            : _description,
        imageUrl: _type == "فيلا"
            ? "https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?w=800&q=80"
            : "https://images.unsplash.com/photo-1545324418-cc1a3fa10c00?w=800&q=80",
        amenities: ["موقع مميز", "صك إلكتروني", "كهرباء ومياه", "قريب من الخدمات"],
        agentName: "وسيط عقاري معتمد",
        agentPhone: "+966500000000",
        agentLicense: "ترخيص فال: 1200099999",
      );

      context.read<PropertyProvider>().addProperty(newProperty);
      Navigator.pop(context);

      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text("تمت إضافة إعلانك بنجاح!"),
          backgroundColor: AppTheme.tagGreenBg,
        ),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("إضافة إعلان عقاري جديد"),
      ),
      body: Form(
        key: _formKey,
        child: ListView(
          padding: const EdgeInsets.all(16),
          children: [
            // الغرض
            const Text("الغرض من الإعلان", style: TextStyle(fontWeight: FontWeight.bold)),
            const SizedBox(height: 8),
            Row(
              children: _purposes.map((p) {
                final isSelected = _purpose == p;
                return Padding(
                  padding: const EdgeInsets.only(left: 8),
                  child: ChoiceChip(
                    label: Text(p),
                    selected: isSelected,
                    selectedColor: AppTheme.goldAccent,
                    labelStyle: TextStyle(
                      color: isSelected ? AppTheme.primaryOnColor : AppTheme.textMain,
                      fontWeight: FontWeight.bold,
                    ),
                    onSelected: (_) => setState(() => _purpose = p),
                  ),
                );
              }).toList(),
            ),
            const SizedBox(height: 16),

            // نوع العقار
            const Text("نوع العقار", style: TextStyle(fontWeight: FontWeight.bold)),
            const SizedBox(height: 8),
            Row(
              children: _types.map((t) {
                final isSelected = _type == t;
                return Padding(
                  padding: const EdgeInsets.only(left: 8),
                  child: ChoiceChip(
                    label: Text(t),
                    selected: isSelected,
                    selectedColor: AppTheme.goldAccent,
                    labelStyle: TextStyle(
                      color: isSelected ? AppTheme.primaryOnColor : AppTheme.textMain,
                      fontWeight: FontWeight.bold,
                    ),
                    onSelected: (_) => setState(() => _type = t),
                  ),
                );
              }).toList(),
            ),
            const SizedBox(height: 16),

            // عنوان الإعلان
            TextFormField(
              decoration: const InputDecoration(
                labelText: "عنوان الإعلان (مثال: فيلا مودرن للبيع)",
                prefixIcon: Icon(Icons.title, color: AppTheme.goldAccent),
              ),
              validator: (v) => v == null || v.trim().isEmpty ? "يرجى كتابة عنوان الإعلان" : null,
              onSaved: (v) => _title = v!.trim(),
            ),
            const SizedBox(height: 14),

            // السعر
            TextFormField(
              keyboardType: TextInputType.number,
              decoration: const InputDecoration(
                labelText: "السعر (ر.س)",
                prefixIcon: Icon(Icons.attach_money, color: AppTheme.goldAccent),
              ),
              validator: (v) {
                if (v == null || v.trim().isEmpty) return "يرجى كتابة السعر";
                if (double.tryParse(v) == null || double.parse(v) <= 0) return "يرجى إدخال سعر صحيح";
                return null;
              },
              onSaved: (v) => _price = double.parse(v!.trim()),
            ),
            const SizedBox(height: 16),

            // المدينة
            DropdownButtonFormField<String>(
              value: _city,
              decoration: const InputDecoration(
                labelText: "المدينة",
                prefixIcon: Icon(Icons.location_city, color: AppTheme.goldAccent),
              ),
              items: _cities.map((c) => DropdownMenuItem(value: c, child: Text(c))).toList(),
              onChanged: (v) => setState(() => _city = v!),
            ),
            const SizedBox(height: 14),

            // الحي
            TextFormField(
              decoration: const InputDecoration(
                labelText: "الحي (مثال: حطين، الياسمين)",
                prefixIcon: Icon(Icons.place, color: AppTheme.goldAccent),
              ),
              validator: (v) => v == null || v.trim().isEmpty ? "يرجى إدخال اسم الحي" : null,
              onSaved: (v) => _neighborhood = v!.trim(),
            ),
            const SizedBox(height: 14),

            // المساحة والغرف والحمامات
            Row(
              children: [
                Expanded(
                  child: TextFormField(
                    keyboardType: TextInputType.number,
                    decoration: const InputDecoration(labelText: "المساحة م²"),
                    validator: (v) => v == null || v.isEmpty ? "المساحة مطلوبة" : null,
                    onSaved: (v) => _area = double.tryParse(v ?? '') ?? 150,
                  ),
                ),
                const SizedBox(width: 8),
                Expanded(
                  child: TextFormField(
                    keyboardType: TextInputType.number,
                    initialValue: "3",
                    decoration: const InputDecoration(labelText: "الغرف"),
                    onSaved: (v) => _bedrooms = int.tryParse(v ?? '') ?? 3,
                  ),
                ),
                const SizedBox(width: 8),
                Expanded(
                  child: TextFormField(
                    keyboardType: TextInputType.number,
                    initialValue: "2",
                    decoration: const InputDecoration(labelText: "الحمامات"),
                    onSaved: (v) => _bathrooms = int.tryParse(v ?? '') ?? 2,
                  ),
                ),
              ],
            ),
            const SizedBox(height: 14),

            // الوصف
            TextFormField(
              maxLines: 3,
              decoration: const InputDecoration(
                labelText: "تفاصيل ووصف العقار (اختياري)",
                alignLabelWithHint: true,
              ),
              onSaved: (v) => _description = v ?? '',
            ),
            const SizedBox(height: 24),

            // زر الحفظ
            ElevatedButton(
              style: ElevatedButton.styleFrom(
                backgroundColor: AppTheme.goldAccent,
                foregroundColor: AppTheme.primaryOnColor,
                padding: const EdgeInsets.symmetric(vertical: 14),
                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
              ),
              onPressed: _submit,
              child: const Text("نشر الإعلان الآن", style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
            ),
            const SizedBox(height: 30),
          ],
        ),
      ),
    );
  }
}
