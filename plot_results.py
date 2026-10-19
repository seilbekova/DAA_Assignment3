import pandas as pd
import matplotlib.pyplot as plt

# Читаем результаты benchmark
df = pd.read_csv("results/results.csv")

# Определяем плотность по названию графа
df["density"] = df["graph"].apply(
    lambda x: "sparse" if x.startswith("sparse") else "dense"
)

# Общее количество операций
df["operations"] = (
    df["dsu_ops"]
    + df["edge_scans"]
    + df["comparisons"]
)

# =========================
# PLOT 1: Operations vs n
# =========================

plt.figure(figsize=(10, 6))

for algorithm in df["algorithm"].unique():

    for density in ["sparse", "dense"]:

        part = df[
            (df["algorithm"] == algorithm)
            & (df["density"] == density)
        ]

        plt.plot(
            part["n"],
            part["operations"],
            marker="o",
            label=f"{algorithm} {density}"
        )

plt.xscale("log")
plt.yscale("log")

plt.xlabel("Number of vertices n")
plt.ylabel("Operation count")
plt.title("Operations vs n")
plt.legend(fontsize=7)
plt.grid(True)

plt.tight_layout()
plt.savefig(
    "results/plots/operations_vs_n.png",
    dpi=300
)

plt.close()


# =========================
# PLOT 2: Scans per edge
# =========================

df["scans_per_edge"] = (
    df["edge_scans"] / df["m"]
)

plt.figure(figsize=(10, 6))

for algorithm in df["algorithm"].unique():

    for density in ["sparse", "dense"]:

        part = df[
            (df["algorithm"] == algorithm)
            & (df["density"] == density)
        ]

        plt.plot(
            part["n"],
            part["scans_per_edge"],
            marker="o",
            label=f"{algorithm} {density}"
        )

plt.xscale("log")

plt.xlabel("Number of vertices n")
plt.ylabel("edgeScans / m")
plt.title("Scans per edge")
plt.legend(fontsize=7)
plt.grid(True)

plt.tight_layout()
plt.savefig(
    "results/plots/scans_per_edge.png",
    dpi=300
)

plt.close()


# ======================================
# PLOT 3: Kruskal share of examined edges
# ======================================

kruskal = df[
    df["algorithm"] == "Kruskal"
].copy()

kruskal["edge_share"] = (
    kruskal["edge_scans"] / kruskal["m"]
)

plt.figure(figsize=(9, 6))

for density in ["sparse", "dense"]:

    part = kruskal[
        kruskal["density"] == density
    ]

    plt.plot(
        part["n"],
        part["edge_share"],
        marker="o",
        label=density
    )

plt.xscale("log")

plt.xlabel("Number of vertices n")
plt.ylabel("edgeScans / m")
plt.title("Share of edges examined by Kruskal")
plt.legend()
plt.grid(True)

plt.tight_layout()
plt.savefig(
    "results/plots/kruskal_edge_share.png",
    dpi=300
)

plt.close()

print("Plots created successfully!")